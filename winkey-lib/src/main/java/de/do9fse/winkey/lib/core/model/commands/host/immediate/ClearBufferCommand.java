package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2
)
public record ClearBufferCommand() implements ImmediateCommand {
    @Override
    public byte[] toData() {
        return new byte[] { 0x0a };
    }
}
