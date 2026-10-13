package de.do9fse.winkey.lib.core.model.commands.admin;

import java.text.MessageFormat;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.responses.EchoResponse;

@ImmediateCommandConfiguration(
    allowedProtocols = { ProtocolVersion.V1, ProtocolVersion.V2 },
    responseType = EchoResponse.class
)
public record EchoTestCommand(char echoChar) implements AdminCommand {
    private static final char MIN_PRINTABLE_ASCII = 0x20;
    private static final char MAX_PRINTABLE_ASCII = 0x7E;

    public EchoTestCommand {
        if (echoChar < MIN_PRINTABLE_ASCII || echoChar > MAX_PRINTABLE_ASCII) {
            final String message = MessageFormat.format(
                "Echo character must be printable ASCII between {0} and {1} but was {2}",
                (int) MIN_PRINTABLE_ASCII,
                (int) MAX_PRINTABLE_ASCII,
                (int) echoChar
            );
            
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public byte[] toData() {
        return new byte[]{ 0x00, 4, (byte) echoChar };
    }
}
