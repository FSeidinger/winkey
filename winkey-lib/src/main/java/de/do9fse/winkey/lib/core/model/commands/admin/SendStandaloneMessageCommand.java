package de.do9fse.winkey.lib.core.model.commands.admin;

import java.text.MessageFormat;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;

@ImmediateCommandConfiguration(
    allowedProtocols = { ProtocolVersion.V1, ProtocolVersion.V2 }
)
public record SendStandaloneMessageCommand(int messageId) implements AdminCommand {
    private static final int MESSAGE_ID_MIN = 0;
    private static final int MESSAGE_ID_MAX = 6;

    public SendStandaloneMessageCommand {
        if (messageId < MESSAGE_ID_MIN || messageId > MESSAGE_ID_MAX) {
            final String message = MessageFormat.format(
                "Message ID must be between {0} and {1} but was {2}",
                MESSAGE_ID_MIN,
                MESSAGE_ID_MAX,
                messageId
            );
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public byte[] toData() {
        return new byte[]{ 0x00, 14, (byte) messageId };
    }
}
