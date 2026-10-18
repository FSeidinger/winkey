package de.do9fse.winkey.lib.core.model.commands.admin;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2
)
public record Reserved3Command() implements AdminCommand {
    @Override
    public byte[] toData() {
        return new byte[] { 0x00, 0x13 };
    }
}
