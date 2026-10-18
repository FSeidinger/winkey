package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.FarnsworthSpeed;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2
)
public record SetFarnsworthSpeedCommand(FarnsworthSpeed speed) implements ImmediateCommand {
    public SetFarnsworthSpeedCommand {
        Validations.validateNotNull("Farnsworth speed", speed);
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x0d, (byte) speed.toProtocolValue() };
    }
}
