package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.CommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.PaddleSetpoint;
import de.do9fse.winkey.lib.core.model.Validations;

@CommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2)
public record SetPaddleSwitchPointCommand(PaddleSetpoint setpoint) implements HostModeCommand {
    public SetPaddleSwitchPointCommand {
        Validations.validateNotNull("Paddle switch point", setpoint);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x12, (byte) setpoint.toProtocolValue() };
    }
}
