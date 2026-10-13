package de.do9fse.winkey.terminal;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jline.shell.CommandDescription;
import org.jline.shell.CommandSession;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.ApplicationState;
import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;
import de.do9fse.winkey.lib.core.model.error.WinKeyApplicationException;
import de.do9fse.winkey.lib.core.port.out.Transport;
import de.do9fse.winkey.lib.infrastructure.winkey.transport.serial.SerialConfigurationAdapter;
import de.do9fse.winkey.lib.infrastructure.winkey.transport.serial.SerialTransport;

public class ConnectCommand extends TerminalCommand {
    protected ConnectCommand() {
        super("connect", "co" );
    }

    @Override
    public String description() {
        return "Connect to a WinKey device";
    }

    @Override
    public Object execute(final CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        if (scanner.hasNext()) {
            throw new IllegalArgumentException("Error: the connect command does not support parameters");
        }

        final ApplicationContext applicationContext = Constants.getApplicationContext(session);
        if (applicationContext.getState() != ApplicationState.CLOSED) {
            throw new IllegalStateException(Errors.DEVICE_ALREADY_OPEN.createErrorMessage(applicationContext));
        } 

        final SerialConfiguration deviceConfiguration = applicationContext.getDeviceConfiguration();

        try {
            final SerialPort serialPort = createSerialPort(stderr, applicationContext, deviceConfiguration);
            openAndInitializeTransport(stderr, applicationContext, serialPort);
        } catch (final SerialPortInvalidPortException e) {
            throw new IllegalArgumentException(Errors.INVALID_PORT.createErrorMessage(applicationContext));
        } catch (final NoSuchElementException e) {
            throw new IllegalStateException(Errors.NO_PORT_SET.createErrorMessage(applicationContext), e);
        }

        final String message = MessageFormat.format("Successfully connected to device {0}", applicationContext.getPortName());
        stdout.println(message);

        return null;
    }

    private SerialPort createSerialPort(final PrintStream stderr, final ApplicationContext context, final SerialConfiguration deviceConfiguration) throws SerialPortInvalidPortException {
        final SerialConfigurationAdapter adapter = new SerialConfigurationAdapter(context, deviceConfiguration);
        return adapter.createSerialPort();
    }

    private void openAndInitializeTransport(final PrintStream stderr, final ApplicationContext context, final SerialPort serialPort) throws WinKeyApplicationException, TimeoutException {
        Transport transport = null;

        try {
            transport = new SerialTransport(context, serialPort);
            context.setTransport(transport);

            transport.open();
            transport.initialize(10, TimeUnit.SECONDS);
        } finally {
            if (context.getState() != ApplicationState.READY) {
                closeTransport(transport);
            }
        }
    }

    private void closeTransport(final Transport transport) {
        try {
            transport.close();
        } catch (final WinKeyApplicationException e) {
            // Silently swallow
        }
    }

    @Override
    public CommandDescription describe(final List<String> args) {
        return CommandDescription
            .builder()
            .mainDescription(
                List.of(
                    new AttributedString(
                        "Usage: connect",
                        AttributedStyle.DEFAULT.bold()
                    ),
                    new AttributedString("Connecting means the the port to the device is initialized with the communication"),
                    new AttributedString("parameters set by 'configure-device'. The device is then opened and an echo command"),
                    new AttributedString("is send to the device. If the device answers the echo command, the connections is"),
                    new AttributedString("regarded as successful.")
                )
            )
            .build();
    }
}
