package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.FirstExtensionDelay;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2
)
public record SetFirstExtensionCommand(FirstExtensionDelay delay) implements ImmediateCommand {
    public SetFirstExtensionCommand {
        Validations.validateNotNull("First extension delay", delay);
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x10, (byte) delay.toProtocolValue() };
    }
}
