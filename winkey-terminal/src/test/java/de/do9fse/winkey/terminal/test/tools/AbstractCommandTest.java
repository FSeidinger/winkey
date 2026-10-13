package de.do9fse.winkey.terminal.test.tools;

import org.jline.shell.CommandSession;
import org.junit.jupiter.api.BeforeEach;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.terminal.Constants;
import de.do9fse.winkey.terminal.MessageGenerator;

/**
 * A base helper class to test JLink commands.
 */
public abstract class AbstractCommandTest {
    /**
     * The command session used in this test
     */
    protected CommandSession session;

    /**
     * The application context used in this test
     */
    protected ApplicationContext context;

    /**
     * The message generator used in this test
     */
    protected MessageGenerator generator;

    /**
     * Sets up dependencies for our command classes
     */
    @BeforeEach
    protected void setupBaseTest() {
        context = new ApplicationContext(ProtocolVersion.V1);
        generator = new MessageGenerator();

        session = new CommandSession();
        session.put(Constants.APPLICATION_CONTEXT_KEY, this.context);
        session.put(Constants.MESSAGE_GENERATOR_KEY, generator);
    }
}
