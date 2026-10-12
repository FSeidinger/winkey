package de.do9fse.winkey.terminal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jline.shell.CommandSession;
import org.jline.shell.impl.DefaultCommandDispatcher;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.terminal.test.tools.ArgumentsBuilder;

public class TerminalCommandTest {
    @Test
    void assertThatContextIsInitializedByExecuteMethod() throws Exception {
        final RuntimeContext context = setupRuntimeContext();
        final TerminalTestCommand command = new TerminalTestCommand();
        final String[] arguments = ArgumentsBuilder.EMPTY;

        command.execute(context.session(), arguments);

        assertNotNull(command.stdout);
        assertNotNull(command.stderr);

        assertNotNull(command.generator);
        assertNotNull(command.context);

        assertNotNull(command.scanner);
    }

    private record RuntimeContext(DefaultCommandDispatcher dispatcher, CommandSession session) {}

    private class TerminalTestCommand extends TerminalCommand {
        public TerminalTestCommand() {
            super("terminal-test-command", new String[0]);
        }
    }

    private RuntimeContext setupRuntimeContext() throws Exception {
        final MessageGenerator generator = new MessageGenerator();
        final ApplicationContext context = new ApplicationContext(WinKeyProtocolVersion.V1);

        final Terminal terminal = TerminalBuilder.builder().system(true).build();
        final DefaultCommandDispatcher dispatcher = new DefaultCommandDispatcher(terminal);

        final CommandSession session = dispatcher.session();
        session.put(Constants.MESSAGE_GENERATOR_KEY, generator);
        session.put(Constants.APPLICATION_CONTEXT_KEY, context);

        return new RuntimeContext(dispatcher, session);
    }
}
