package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.KeyCompensation;
import de.do9fse.winkey.lib.core.model.Validations;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record SetKeyCompensationCommand(KeyCompensation compensation) implements HostModeCommand {
    public SetKeyCompensationCommand {
        Validations.validateNotNull("Key compensation", compensation);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x11, (byte) compensation.toProtocolValue() };
    }
}
