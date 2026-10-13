package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record NullCommand() implements HostModeCommand {
    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x13 };
    }
}
