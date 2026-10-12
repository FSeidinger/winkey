package de.do9fse.winkey.terminal;

import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.jline.shell.ArgumentDescription;
import org.jline.shell.CommandDescription;
import org.jline.shell.CommandSession;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;

public class SetVersionCommand extends TerminalCommand {
    public SetVersionCommand() {
        super("set-version", "v", "sv");
    }

    @Override
    public String description() {
        return "Set the WinKey version to be used with the keyer device";
    }

    @Override
    public Object execute(CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        final String versionString;
        final WinKeyProtocolVersion version;

        try {
            versionString = scanner.next().toUpperCase();
        } catch (final NoSuchElementException e) {
            throw new NoSuchElementException("The version parameter is mandatory");
        }

        try {
            version = WinKeyProtocolVersion.valueOf(versionString);
            context.setVersion(version);
        } catch (final IllegalArgumentException e) {
            final Set<WinKeyProtocolVersion> allowedValues = EnumSet.allOf(WinKeyProtocolVersion.class);
            throw new IllegalArgumentException("Version must be one of " + allowedValues + " but was " + versionString);
        }

        stdout.println("Version " + version + " selected");
        return null;
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
}
