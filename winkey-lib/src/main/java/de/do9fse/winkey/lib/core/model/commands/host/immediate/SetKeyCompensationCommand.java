package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.KeyCompensation;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2
)
public record SetKeyCompensationCommand(KeyCompensation compensation) implements ImmediateCommand {
    public SetKeyCompensationCommand {
        Validations.validateNotNull("Key compensation", compensation);
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x11, (byte) compensation.toProtocolValue() };
    }
}
