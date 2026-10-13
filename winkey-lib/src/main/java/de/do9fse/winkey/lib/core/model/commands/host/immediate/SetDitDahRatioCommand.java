package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.CommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.DitDahRatio;
import de.do9fse.winkey.lib.core.model.Validations;

@CommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2)
public record SetDitDahRatioCommand(DitDahRatio ratio) implements HostModeCommand {
    public SetDitDahRatioCommand {
        Validations.validateNotNull("Dit/dah ratio", ratio);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x17, (byte) ratio.toProtocolValue() };
    }
}
