package de.do9fse.winkey.lib.core.model.commands.test.suite5;

import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.responses.WinKeyVersionResponse;

@ImmediateCommandConfiguration(
    allowedProtocols = { WinKeyProtocolVersion.V1, WinKeyProtocolVersion.V2, WinKeyProtocolVersion.V3},
    responseType = WinKeyVersionResponse.class
)
public record AdminCommandWithResponse() implements AdminCommand {
    @Override
    public byte[] getPayloadBytes() {
        return null;
    }
}
