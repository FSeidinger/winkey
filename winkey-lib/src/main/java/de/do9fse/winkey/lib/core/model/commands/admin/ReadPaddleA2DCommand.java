package de.do9fse.winkey.lib.core.model.commands.admin;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.responses.PaddleA2DResponse;

@ImmediateCommandConfiguration(
    allowedProtocols = { ProtocolVersion.V1, ProtocolVersion.V2 },
    responseType = PaddleA2DResponse.class
)
public record ReadPaddleA2DCommand() implements AdminCommand {
    @Override
    public byte[] toData() {
        return new byte[]{ 0x00, 5 };
    }
}
