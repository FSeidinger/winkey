package de.do9fse.winkey.terminal;

import org.jline.shell.CommandSession;

public class GetVersionCommand extends TerminalCommand {
    public GetVersionCommand() {
        super("get-version", "v", "gv");
    }

    @Override
    public String description() {
        return "Get the current device version";
    }

    @Override
    public Object execute(final CommandSession session, final String[] args) throws Exception {
        super.execute(session, args);

        if (scanner.hasNext()) {
             throw new IllegalArgumentException("Error: the disconnect command does not support parameters");
        }

        stdout.println("Current protocol version is: " + context.getProtocolVersion());
        stdout.println("Current device version is: " + context.getDeviceVersion().getVersionString());

        return null;
    }
}
