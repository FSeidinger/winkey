package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.ModeRegister;
import de.do9fse.winkey.lib.core.model.Validations;

@ImmediateCommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2)
public record SetModeCommand(ModeRegister mode) implements HostModeCommand {
    public SetModeCommand {
        Validations.validateNotNull("Mode register", mode);
        Validations.validateNotNull("Key mode", mode.keyMode());
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x0e, (byte) mode.toProtocolValue() };
    }
}
