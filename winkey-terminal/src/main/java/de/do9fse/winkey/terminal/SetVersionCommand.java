package de.do9fse.winkey.terminal;

import java.io.PrintStream;
import java.util.List;

import org.jline.shell.ArgumentDescription;
import org.jline.shell.CommandDescription;
import org.jline.shell.CommandSession;
import org.jline.shell.impl.AbstractCommand;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;

public class SetVersionCommand extends AbstractCommand {
    public SetVersionCommand() {
        super("set-version", "v", "sv");
    }

    @Override
    public String description() {
        return "Set the WinKey version to be used with the keyer device";
    }

    @Override
    public CommandDescription describe(final List<String> args) {
        return CommandDescription
            .builder()
            .mainDescription(
                List.of(
                    new AttributedString(
                        "Usage: set-version <version>",
                        AttributedStyle.DEFAULT.bold()
                    )
                )
            )
            .argument(
                new ArgumentDescription(
                    "<version>",
                     List.of(
                        new AttributedString("The WinKey protocol version to use in the form Vx"),
                        new AttributedString("where x can be 1, 2, or 3")
                     )
                )
            )
            .build();
    }

    @Override
    public Object execute(CommandSession session, String[] args) throws Exception {
        final PrintStream stdout = session.out();
        final PrintStream stderr = session.err();

        if (args.length != 1) {
            stderr.println("usage: set-version <version>");
            stderr.println("  <version>     The WinKey protocol version to use (V1, V2 or V3");
            return null;
        }

        try {
            final String versionString = args[0].toUpperCase();
            final WinKeyProtocolVersion version = WinKeyProtocolVersion.valueOf(versionString);
            final ApplicationContext context = (ApplicationContext) session.get(Constants.APPLICATION_CONTEXT_KEY);
            context.setVersion(version);

            stdout.println("Version " + version + " selected");
        } catch (final NullPointerException | IllegalArgumentException e) {
            stderr.println(e.getMessage());
        }

        return null;
    }
}
