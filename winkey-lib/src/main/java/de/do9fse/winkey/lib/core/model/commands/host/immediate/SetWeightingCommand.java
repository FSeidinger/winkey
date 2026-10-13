package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.Weighting;
import de.do9fse.winkey.lib.core.model.Validations;

@ImmediateCommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2)
public record SetWeightingCommand(Weighting weighting) implements HostModeCommand {
    public SetWeightingCommand {
        Validations.validateNotNull("Weighting", weighting);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x03, (byte) weighting.toProtocolValue() };
    }
}
