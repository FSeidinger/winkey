package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.ModeRegister;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record SetModeCommand(ModeRegister mode) implements ImmediateCommand {
    public SetModeCommand {
        Validations.validateNotNull("Mode register", mode);
        Validations.validateNotNull("Key mode", mode.keyMode());
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x0e, (byte) mode.toProtocolValue() };
    }
}
