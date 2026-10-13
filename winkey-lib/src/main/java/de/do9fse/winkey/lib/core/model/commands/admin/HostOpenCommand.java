package de.do9fse.winkey.lib.core.model.commands.admin;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.responses.VersionResponse;

@ImmediateCommandConfiguration(
    allowedProtocols = { ProtocolVersion.V1, ProtocolVersion.V2 },
    responseType = VersionResponse.class
)
public record HostOpenCommand() implements AdminCommand {
    @Override
    public byte[] toData() {
        return new byte[] { 0x00, 2 };
    }
}
