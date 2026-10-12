package de.do9fse.winkey.terminal;

import java.io.IOException;
import java.lang.reflect.Method;

import org.jline.shell.CommandGroup;
import org.jline.shell.CommandSession;
import org.jline.shell.Shell;
import org.jline.shell.impl.SimpleCommandGroup;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
   
    public void run() throws IOException {
        setRootLogLevel("OFF");

        final MessageGenerator generator = new MessageGenerator();
        final ApplicationContext context = new ApplicationContext(WinKeyProtocolVersion.V1);

        final Terminal terminal = TerminalBuilder.builder().system(true).build();
        final ErrorHandlingCommandDispatcher dispatcher = new ErrorHandlingCommandDispatcher(terminal);

        final CommandSession session = dispatcher.session();
        session.put(Constants.MESSAGE_GENERATOR_KEY, generator);
        session.put(Constants.APPLICATION_CONTEXT_KEY, context);

        final CommandGroup applicationCommands = new SimpleCommandGroup(
            "application",
            new SetVersionCommand(),
            new ConfigureDeviceCommand()
        );

        final CommandGroup transportCommands = new SimpleCommandGroup(
            "WinKey",
            new ConnectCommand(),
            new DisconnectCommand(),
            new EnterHostModeCommand(),
            new LeaveHostModeCommand()
        );

        try (final Shell shell = Shell
            .builder()
            .terminal(terminal)
            .dispatcher(dispatcher)
            .groups(applicationCommands, transportCommands)
            .helpCommands(true)
            .prompt("cw> ")
            .build()
        ) {
            shell.run();
        } catch (final Exception e) {
            LOGGER.error("Shell failed", e);
        }
    }

    public static void main(String[] args) throws IOException {
        final Main main = new Main();
        main.run();
   }

    private void setRootLogLevel(String levelName) {
        try {
            final Logger slf4jLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            final Class<?> levelClass = Class.forName("ch.qos.logback.classic.Level");
            
            final Method toLevelMethod = levelClass.getMethod("toLevel", String.class);
            final Object levelObject = toLevelMethod.invoke(null, levelName);

            final Method setLevelMethod = slf4jLogger.getClass().getMethod("setLevel", levelClass);
            setLevelMethod.invoke(slf4jLogger, levelObject);

        } catch (final ReflectiveOperationException e) {
            LOGGER.error("Failed to set log level", e);
        }
    }
}
