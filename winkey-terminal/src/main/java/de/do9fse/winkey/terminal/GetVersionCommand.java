package de.do9fse.winkey.terminal;

import org.jline.shell.CommandSession;

public class GetVersionCommand extends TerminalCommand {
    public GetVersionCommand() {
        super("get-version", "v", "gv");
    }

    @Override
    public String description() {
        return "Get the protocol version currently set";
    }

        @Override
    public Object execute(final CommandSession session, final String[] args) throws Exception {
        super.execute(session, args);

        return null;
    }
}
