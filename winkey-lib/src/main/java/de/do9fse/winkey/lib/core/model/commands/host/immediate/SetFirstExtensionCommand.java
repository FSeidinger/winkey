package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.FirstExtensionDelay;
import de.do9fse.winkey.lib.core.model.Validations;

@ImmediateCommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2)
public record SetFirstExtensionCommand(FirstExtensionDelay delay) implements HostModeCommand {
    public SetFirstExtensionCommand {
        Validations.validateNotNull("First extension delay", delay);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x10, (byte) delay.toProtocolValue() };
    }
}
