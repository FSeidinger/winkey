package de.do9fse.winkey.lib.core.model.commands.admin;

import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.responses.PaddleA2DResponse;

@ImmediateCommandConfiguration(
    allowedProtocols = { WinKeyProtocolVersion.V1, WinKeyProtocolVersion.V2 },
    responseType = PaddleA2DResponse.class
)
public record ReadPaddleA2DCommand() implements AdminCommand {
    @Override
    public byte[] getPayloadBytes() {
        return new byte[]{ 0x00, 5 };
    }
}
