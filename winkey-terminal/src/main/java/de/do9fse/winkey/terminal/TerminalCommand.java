package de.do9fse.winkey.terminal;

import java.io.PrintStream;

import org.jline.shell.CommandSession;
import org.jline.shell.impl.AbstractCommand;

import de.do9fse.winkey.lib.core.model.ApplicationContext;

public abstract class TerminalCommand extends AbstractCommand {
    protected PrintStream stdout;
    protected PrintStream stderr;

    protected ErrorHandlingCommandDispatcher dispatcher;
    protected MessageGenerator generator;
    protected ApplicationContext context;

    protected ArgumentScanner scanner;

    public TerminalCommand(String name, String... aliases) {
        super(name, aliases);
    }
    
    @Override
    public Object execute(final CommandSession session, final String[] args) throws Exception {
        return evaluateContext(session, args);
    }

    private Object evaluateContext(final CommandSession session, final String[] args) throws Exception {
        this.stdout = session.out();
        this.stderr = session.err();

        this.dispatcher = (ErrorHandlingCommandDispatcher) session.get(Constants.DISPATCHER_KEY);
        this.generator = (MessageGenerator) session.get(Constants.MESSAGE_GENERATOR_KEY);
        this.context = (ApplicationContext) session.get(Constants.APPLICATION_CONTEXT_KEY);

        this.scanner = new ArgumentScanner(args);

        return null;
    }
}
