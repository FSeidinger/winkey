package de.do9fse.winkey.terminal;

import java.text.MessageFormat;
import java.util.List;

import org.jline.shell.CommandDescription;
import org.jline.shell.CommandSession;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import de.do9fse.winkey.lib.core.model.ApplicationState;
import de.do9fse.winkey.lib.core.port.out.Transport;

public class DisconnectCommand extends TerminalCommand {
    protected DisconnectCommand() {
        super("disconnect", "dc" );
    }

    @Override
    public String description() {
        return "disconnect from the device";
    }

    @Override
    public Object execute(final CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        if (scanner.hasNext()) {
             throw new IllegalArgumentException("Error: the disconnect command does not support parameters");
        }

        if (context.getState() == ApplicationState.CLOSED) {
            throw new IllegalStateException(Errors.DEVICE_ALREADY_CLOSED.createErrorMessage(context));
        }

        final Transport transport = context.getTransport();
        transport.close();

        final String message = MessageFormat.format("Successfully disconnected device {0}", context.getPortName());
        stdout.println(message);

        return null;
    }

    @Override
    public CommandDescription describe(final List<String> args) {
        return CommandDescription
            .builder()
            .mainDescription(
                List.of(
                    new AttributedString(
                        "Usage: disconnect",
                        AttributedStyle.DEFAULT.bold()
                    ),
                    new AttributedString("Disconnecting means that the device is closed and the application is reset to its"),
                    new AttributedString("startup state. The device can then be opened again with the 'connect' command. ")
                )
            )
            .build();
    }
}
