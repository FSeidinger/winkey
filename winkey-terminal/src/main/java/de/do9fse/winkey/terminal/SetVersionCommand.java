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

import de.do9fse.winkey.lib.core.model.DeviceVersion;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;

public class SetVersionCommand extends TerminalCommand {
    public SetVersionCommand() {
        super("set-version", "sv");
    }

    @Override
    public String description() {
        return "Set the protocol version to be used with the keyer device";
    }

    @Override
    public Object execute(CommandSession session, String[] args) throws Exception {
        super.execute(session, args);

        final String versionParameter;
        try {
            versionParameter = scanner.next().toUpperCase();
        } catch (final NoSuchElementException e) {
            throw new NoSuchElementException("The version parameter is mandatory");
        }

        final DeviceVersion version;
        try {
            final ProtocolVersion protocolVersion = ProtocolVersion.valueOf(versionParameter);
            version = protocolVersion.toDeviceVersion();
            context.setDeviceVersion(version);
        } catch (final IllegalArgumentException e) {
            final Set<ProtocolVersion> allowedValues = EnumSet.allOf(ProtocolVersion.class);
            throw new IllegalArgumentException("Version must be one of " + allowedValues + " but was " + versionParameter);
        }

        stdout.println("Version " + version.toProtocolVersion() + " selected");
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
                        new AttributedString("The protocol version to use in the form Vx"),
                        new AttributedString("where x can be 1, 2, or 3")
                     )
                )
            )
            .build();
    }
}
