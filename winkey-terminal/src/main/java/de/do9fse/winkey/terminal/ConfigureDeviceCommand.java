package de.do9fse.winkey.terminal;

import java.util.List;

import org.jline.shell.ArgumentDescription;
import org.jline.shell.CommandDescription;
import org.jline.shell.CommandSession;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import de.do9fse.winkey.lib.core.model.configuration.serial.BaudRate;
import de.do9fse.winkey.lib.core.model.configuration.serial.DataBits;
import de.do9fse.winkey.lib.core.model.configuration.serial.DeviceConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.serial.Parity;
import de.do9fse.winkey.lib.core.model.configuration.serial.StopBits;
import de.do9fse.winkey.lib.core.model.configuration.serial.Timeout;
import de.do9fse.winkey.terminal.ArgumentScanner.CommandOption;

public class ConfigureDeviceCommand extends TerminalCommand {
    protected ConfigureDeviceCommand() {
        super("configure-device", new String[0]);
    }

    @Override
    public String description() {
        return "Configure the serial device";
    }

    @Override
    public Object execute(CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        String portName = null;
        BaudRate baudRate = BaudRate.DEFAULT;

        while (scanner.hasNext()) {
            if (scanner.peekOption() != null) {
                final CommandOption commandOption = scanner.nextOption();                    

                switch (commandOption.option()) {
                    case "-b", "--baudRate":
                        baudRate = BaudRate.parseCustomBaudRate(commandOption.value());
                        break;

                    case "-d", "--defaultBaudRate":
                        baudRate = BaudRate.parseDefaultBaudRate(commandOption.value());
                        break;

                    default: throw new IllegalArgumentException("Unexpected option " + commandOption.option());
                }

                // Handle next argument
                continue;
            }

            portName = scanner.next();
            if (scanner.hasNext()) {
                throw new IllegalArgumentException("Unexpected argument " + scanner.next());
            }
        }

        if (portName == null) {
            throw new IllegalArgumentException("The <device> parameter is mandatory");
        }
       
        context.setPortName(portName);

        final DeviceConfiguration deviceConfiguration = new DeviceConfiguration(
            baudRate,
            DataBits.DEFAULT,
            Parity.DEFAULT,
            StopBits.DEFAULT,
            Timeout.DEFAULT_INITIALIZATION_TIMEOUT,
            Timeout.DEFAULT_READ_TIMEOUT
        );
        context.setDeviceConfiguration(deviceConfiguration);

        stdout.println("Successfully set device configuration - " + deviceConfiguration);

        return null;
    }

    @Override
    public CommandDescription describe(final List<String> args) {
        return CommandDescription
            .builder()
            .mainDescription(
                List.of(
                    new AttributedString(
                        "Usage: configure-device [[-b baudRate] | [-d defaultBaudRate | alternativeBaudRate]] <device>",
                        AttributedStyle.DEFAULT.bold()
                    )
                )
            )
            .argument(
                new ArgumentDescription("-b",
                    List.of(
                        new AttributedString("Sets a custom baud rate to be used with the device.\n"),
                        new AttributedString("The baud rate must be an integer greater than zero.\n"),
                        new AttributedString("Instead of using a custom baud rate, you can use the '-d' option to set a default"),
                        new AttributedString("baud rate which conforms with the WinKey device specification.")
                    )
                )
            )
            .argument(
                new ArgumentDescription("-d",
                    List.of(
                        new AttributedString("Sets a default baud rate that conforms to the WinKey device specification.\n"),
                        new AttributedString("The value 'defaultBaudRate' selects the default baud rate for nearly all WinKey"),
                        new AttributedString("and translates to 1200 baud.\n"),
                        new AttributedString("The value 'alternativeBaudRate' selects the default baud for special devices"),
                        new AttributedString("using a baud rate of 9600 baud.\n"),
                        new AttributedString("Both the '-b' and '-d' options are optional. If they are not given on the"),
                        new AttributedString("command line the default baud rate of 1200 baud is taken. This is the equivalent"),
                        new AttributedString("of using '-d defaultBaudRate'")
                    )
                )
            )
            .argument(
                new ArgumentDescription("<device>",
                    List.of(
                        new AttributedString("The device (port) the WinKey device is connected to\n"),
                        new AttributedString("On Windows this something like 'COM3'."),
                        new AttributedString("On Linux this something like '/dev/ttyUSB0'.")
                    )
                )
            )
            .build();
    }
}
