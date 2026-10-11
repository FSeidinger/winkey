package de.do9fse.winkey.terminal;

import java.text.MessageFormat;

import org.jline.shell.CommandSession;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.WinKeyState;
import de.do9fse.winkey.lib.core.port.out.WinKeyTransport;

public class DisconnectCommand extends TerminalCommand {
    protected DisconnectCommand() {
        super("disconnect", "dc" );
    }

    @Override
    public String description() {
        return "disconnect the WinKey device";
    }

    @Override
    public Object execute(final CommandSession session, String[] args) throws Exception {
        evaluateContext(session);

        if (args.length != 0) {
             throw new IllegalArgumentException("Error: the disconnect command does not support parameters");
        }

        final ApplicationContext applicationContext = Constants.getApplicationContext(session);
        if (applicationContext.getState() == WinKeyState.CLOSED) {
            throw new IllegalStateException(generator.createErrorMessage(Errors.DEVICE_ALREADY_OPEN, applicationContext));
        }

        final WinKeyTransport transport = applicationContext.getTransport();
        transport.close();

        final String message = MessageFormat.format("Successfully disconnected device {0}", applicationContext.getPortName());
        stdout.println(message);

        return null;
    }
}
