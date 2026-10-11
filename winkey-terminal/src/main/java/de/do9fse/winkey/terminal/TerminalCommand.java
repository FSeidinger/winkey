package de.do9fse.winkey.terminal;

import java.io.PrintStream;

import org.jline.shell.CommandSession;
import org.jline.shell.impl.AbstractCommand;

import de.do9fse.winkey.lib.core.model.ApplicationContext;

public abstract class TerminalCommand extends AbstractCommand {
    protected PrintStream stdout;
    protected PrintStream stderr;

    protected MessageGenerator generator;
    protected ApplicationContext context;

    public TerminalCommand(String name, String... aliases) {
        super(name, aliases);
    }

    public void evaluateContext(final CommandSession session) throws Exception {
        this.stdout = session.out();
        this.stderr = session.err();

        this.generator = (MessageGenerator) session.get(Constants.MESSAGE_GENERATOR_KEY);
        this.context = (ApplicationContext) session.get(Constants.APPLICATION_CONTEXT_KEY);
    }
}
