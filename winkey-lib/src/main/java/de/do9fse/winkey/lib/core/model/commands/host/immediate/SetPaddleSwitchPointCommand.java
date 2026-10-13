package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.PaddleSetpoint;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record SetPaddleSwitchPointCommand(PaddleSetpoint setpoint) implements HostModeCommand {
    public SetPaddleSwitchPointCommand {
        Validations.validateNotNull("Paddle switch point", setpoint);
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x12, (byte) setpoint.toProtocolValue() };
    }
}
