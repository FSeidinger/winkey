package de.do9fse.winkey.lib.core.model.commands.host.buffered;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record PttCommand(boolean enabled) implements HostModeCommand {
    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x18, (byte) (enabled ? 1 : 0) };
    }
}
