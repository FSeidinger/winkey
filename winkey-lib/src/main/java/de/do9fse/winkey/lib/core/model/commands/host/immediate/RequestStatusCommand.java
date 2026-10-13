package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.responses.StatusResponse;

@ImmediateCommandConfiguration(
    allowedProtocols = de.do9fse.winkey.lib.core.model.ProtocolVersion.V2,
    responseType = StatusResponse.class
)
public record RequestStatusCommand() implements HostModeCommand {
    @Override
    public byte[] toData() {
        return new byte[] { 0x15 };
    }
}
