package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.responses.WinKeyStatusResponse;

@ImmediateCommandConfiguration(
    allowedProtocols = de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion.V2,
    responseType = WinKeyStatusResponse.class
)
public record RequestWinKeyStatusCommand() implements HostModeCommand {
    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x15 };
    }
}
