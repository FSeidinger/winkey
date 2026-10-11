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
        evaluateContext(session);

        String portName = null;
        BaudRate baudRate = BaudRate.DEFAULT;

        final ArgumentScanner scanner = new ArgumentScanner(args);
        while (scanner.hasNext()) {
            final String argument = scanner.next();

            switch (argument) {
                case "-b":
                    baudRate = BaudRate.parseCustomBaudRate(scanner.next());
                    break;

                case "-d":
                    baudRate = BaudRate.parseDefaultBaudRate(scanner.next());
                    break;

                default:
                    portName = argument;
                    if (scanner.hasNext()) {
                        throw new IllegalArgumentException("Unexpected argument " + scanner.next());
                    }
                    break;
            }
        }

        if (portName == null) {
            throw new IllegalArgumentException("The <device> parameter is mandatory");
        }
       
        context.setPortName(portName);

        final DeviceConfiguration deviceConfiguration = new DeviceConfiguration(baudRate, DataBits.DEFAULT, Parity.DEFAULT, StopBits.DEFAULT);
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
                        new AttributedString("Instead of using a custom baud rate, you can use the `-d` option to set a default"),
                        new AttributedString("baud rate which conforms with the WinKey device specification.")
                    )
                )
            )
            .argument(
                new ArgumentDescription("-d",
                    List.of(
                        new AttributedString("Sets a default baud rate that conforms to the WinKey device specification.\n"),
                        new AttributedString("The value `defaultBaudRate` selects the default baud rate for nearly all WinKey"),
                        new AttributedString("and translates to 1200 baud.\n"),
                        new AttributedString("The value `alternativeBaudRat` selects the default baud for special devices"),
                        new AttributedString("using a baud rate of 9600 baud.\n"),
                        new AttributedString("Both the `-b` and `-d` options are optional. In they are not given on the"),
                        new AttributedString("command line the default baud rate of 1200 baud is taken. This is the equivalent"),
                        new AttributedString("of using `-d defaultBaudRate`")
                    )
                )
            )
            .build();
    }
}
