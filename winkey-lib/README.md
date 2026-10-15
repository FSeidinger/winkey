# The Winkey Library

`winkey-lib` is a small Java library for talking to WinKey keyers over a serial
connection. It gives you typed commands and responses instead of making you
assemble protocol bytes by hand. If you are here to use it, start with the
model overview and example below; if you are curious about its internals, keep
reading for the architecture and concurrency notes.

## Using the library

The central object is `ApplicationContext`. It holds the protocol version,
device configuration, transport, state and the job queue. Its state moves
through `CLOSED`, `INITIALIZING` and `READY` as the connection is opened and
initialized.

The main `core.model` objects are:

| Object                                                              | What it represents                                                                                                                               |
| ------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| `ApplicationContext`, `ApplicationState`, `ApplicationStateMachine` | Connection-wide configuration and the `CLOSED` → `INITIALIZING` → `READY` lifecycle.                                                             |
| `ProtocolVersion`                                                   | The WinKey protocol version used by the application.                                                                                             |
| `SerialConfiguration`, `BaudRate`, `Timeout` and related types      | Serial settings and timeout values; other configuration records cover keyer settings such as speed, mode and weighting.                          |
| `Command`                                                           | An object that serializes itself to the bytes sent to the keyer. Immediate commands also declare supported protocol versions and response types. |
| `DataItem`                                                          | A serializable item sent to the keyer's host-mode buffer; `TextDataItem` is a simple text example.                                               |
| `Job`                                                               | An immediate command paired with a `CompletableFuture<Response>` for its result.                                                                 |
| `Response`                                                          | A typed interpretation of bytes returned by the device, such as status, version or calibration data.                                             |

Here is a compact version of the setup and command flow used by the library
and terminal entry points:

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.fazecast.jSerialComm.SerialPort;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.admin.HostOpenCommand;
import de.do9fse.winkey.lib.core.model.commands.host.buffered.WaitCommand;
import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;
import de.do9fse.winkey.lib.core.model.data.TextDataItem;
import de.do9fse.winkey.lib.core.model.responses.Response;
import de.do9fse.winkey.lib.core.model.responses.VersionResponse;
import de.do9fse.winkey.lib.core.port.out.Transport;
import de.do9fse.winkey.lib.infrastructure.winkey.transport.serial.SerialConfigurationAdapter;
import de.do9fse.winkey.lib.infrastructure.winkey.transport.serial.SerialTransport;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

public class Example {
    public static void main(String[] args) throws Exception {
        final ApplicationContext context = new ApplicationContext(ProtocolVersion.V2);
        context.setPortName("/dev/ttyUSB0");

        final SerialConfiguration configuration = SerialConfiguration.DEFAULT;
        context.setDeviceConfiguration(configuration);

        final SerialPort port = new SerialConfigurationAdapter(context, configuration).createSerialPort();

        final Transport transport = new SerialTransport(context, port);
        transport.open();
        try {
            transport.initialize(
                configuration.initializationTimeout().timeout(),
                configuration.initializationTimeout().timeUnit()
            );

            // HostOpen is an admin command; its response is a VersionResponse.
            final CompletableFuture<Response> versionResult = new CompletableFuture<>();
            transport.submitJob(new HostOpenCommand(), versionResult);

            // Wait ten seconds for the device to respond.
            final VersionResponse version = (VersionResponse) versionResult.get(10, TimeUnit.SECONDS);
            System.out.println("Keyer version: " + version.getVersionString());

            // Send text and a buffered wait to the keyer's host-mode buffer.
            transport.sendDataItems(
                new TextDataItem("CQ CQ DE DO9FSE"),
                new WaitCommand(Quantities.getQuantity(3, Units.SECOND)),
                new TextDataItem("DE DO9FSE")
            );
        } finally {
            transport.close();
        }
    }
}
```

The example configures the baud rate, serial read timeout and initialization
deadline via `SerialConfiguration`. `SerialConfigurationAdapter` applies the
baud rate and converts the configured read timeout to milliseconds for
jSerialComm before the port is opened. `transport.initialize(...)` then
performs the echo handshake using the configured initialization deadline.
The `get` timeout is separate: it limits how long the calling code waits for
a command response.

## For developers

The code is organized around the **Ports and Adapters** pattern. `core.model`
describes commands, data, configuration and responses. The core ports define the
outgoing `Transport` and `JobQueue` contracts and the incoming
`UnsolicitedResponseListener`. Infrastructure supplies the serial adapter
(`SerialTransport`) and default queue (`DefaultJobQueue`); the terminal is a
driving adapter that turns user input into calls to those ports.

### Command paths: a closer look

- **Admin commands** implement `AdminCommand`, which extends
  `ImmediateCommand`. They cover device-level operations such as opening or
  closing host mode, reading configuration, and resetting the keyer.
  `HostOpenCommand`, for example, sends its command bytes and expects a
  `VersionResponse`. Admin commands use the same job-and-response path as
  other immediate commands.
- **Immediate Host Mode commands** are low-latency commands for changing or
  querying keyer behavior while using host mode. Examples include
  `RequestStatusCommand` and `SetSpeedCommand`. Their command metadata declares
  supported protocol versions and the expected response type. Status bytes are
  currently routed to unsolicited-response listeners by the reader.
- **Buffered Host Mode commands** implement `BufferedCommand`, which is also
  a `DataItem`. They add work to the keyer's transmit buffer—for example,
  text, waits or speed changes—rather than asking the host to wait for a
  response to each item. Send them in order with `sendDataItems(...)`.

### Sender, job queue and background receiver

Once the echo handshake succeeds, `SerialTransport` starts a virtual
`SerialPortReader` thread. It reads in the background while application code
continues to submit work. `submitJob` puts each immediate command and its
response future into the FIFO `JobQueue` and sends its bytes. The receiver
looks at the queue head, reads the number of bytes specified for that
command's response, decodes them and completes the matching future. This keeps
responses associated with the command that requested them.

Queue locking coordinates the sender's enqueue/send operation and the
receiver's dequeue/completion; the FIFO head is the response currently being
awaited. The reader also recognizes unsolicited status and
speed-potentiometer bytes and forwards them to registered listeners,
independently of a pending job. In particular, status bytes are routed to
listeners before active-job response parsing. If there is no active job, other
out-of-band bytes are ignored. A serial read timeout lets the receiver loop
continue when the device has nothing to report; closing the transport
interrupts and stops that reader.