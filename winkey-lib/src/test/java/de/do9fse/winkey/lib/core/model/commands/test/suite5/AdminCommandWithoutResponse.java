package de.do9fse.winkey.lib.core.model.commands.test.suite5;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;

@ImmediateCommandConfiguration(
    allowedProtocols = { ProtocolVersion.V1, ProtocolVersion.V2, ProtocolVersion.V3 }
)
public record AdminCommandWithoutResponse() implements AdminCommand {
    @Override 
    public byte[] toData() {
        return null;
    }
}
