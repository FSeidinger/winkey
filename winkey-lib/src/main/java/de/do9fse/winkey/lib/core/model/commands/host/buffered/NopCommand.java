package de.do9fse.winkey.lib.core.model.commands.host.buffered;

import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;

@ImmediateCommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2)
public record NopCommand() implements HostModeCommand {
    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x1f };
    }
}
