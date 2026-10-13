package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeedWithReset;
import de.do9fse.winkey.lib.core.model.Validations;

@ImmediateCommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2)
public record SetSpeedCommand(WPMSpeedWithReset wpm) implements HostModeCommand {
    public SetSpeedCommand {
        Validations.validateNotNull("WPM speed", wpm);
    }

    public static SetSpeedCommand useSpeedPot() {
        return new SetSpeedCommand(WPMSpeedWithReset.useSpeedPot());
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x02, (byte) wpm.toProtocolValue() };
    }
}
