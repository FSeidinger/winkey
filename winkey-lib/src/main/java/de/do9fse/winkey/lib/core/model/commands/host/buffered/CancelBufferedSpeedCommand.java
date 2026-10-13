package de.do9fse.winkey.lib.core.model.commands.host.buffered;

import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.CommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.host.immediate.HostModeCommand;

@CommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2, isBuffered = true)
public record CancelBufferedSpeedCommand() implements HostModeCommand {
    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x1e };
    }
}
