package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import java.text.MessageFormat;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record MergeLettersCommand(char first, char second) implements HostModeCommand {
    private static final char MAX_ASCII_CHARACTER = 0x7f;

    public MergeLettersCommand {
        if (first > MAX_ASCII_CHARACTER || second > MAX_ASCII_CHARACTER) {
            final String message = MessageFormat.format(
                "Merged letters must be ASCII characters (maximum code point {0})",
                (int) MAX_ASCII_CHARACTER
            );
            
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x1b, (byte) first, (byte) second };
    }
}
