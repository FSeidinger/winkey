package de.do9fse.winkey.lib.core.model.commands.admin;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.XMode;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2
)
public record LoadXModeCommand(XMode xMode) implements AdminCommand {
    public LoadXModeCommand {
        Validations.validateNotNull("XMode", xMode);
    }
    
    @Override
    public byte[] toData() {
        return new byte[]{ 0x00, 0x0F, (byte) xMode.toProtocolValue()};
    }
}
