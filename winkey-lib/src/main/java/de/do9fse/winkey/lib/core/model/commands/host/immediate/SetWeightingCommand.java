package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.Weighting;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2
)
public record SetWeightingCommand(Weighting weighting) implements ImmediateCommand {
    public SetWeightingCommand {
        Validations.validateNotNull("Weighting", weighting);
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x03, (byte) weighting.toProtocolValue() };
    }
}
