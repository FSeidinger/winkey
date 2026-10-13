package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.PinConfiguration;
import de.do9fse.winkey.lib.core.model.Validations;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record SetPinConfigurationCommand(PinConfiguration configuration) implements HostModeCommand {
    public SetPinConfigurationCommand {
        Validations.validateNotNull("Pin configuration", configuration);
        Validations.validateNotNull("Ultimatic priority", configuration.ultimaticPriority());
        Validations.validateNotNull("Paddle hang time", configuration.paddleHangTime());
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x09, (byte) configuration.toProtocolValue() };
    }
}
