package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.FarnsworthSpeed;
import de.do9fse.winkey.lib.core.model.Validations;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record SetFarnsworthSpeedCommand(FarnsworthSpeed speed) implements HostModeCommand {
    public SetFarnsworthSpeedCommand {
        Validations.validateNotNull("Farnsworth speed", speed);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x0d, (byte) speed.toProtocolValue() };
    }
}
