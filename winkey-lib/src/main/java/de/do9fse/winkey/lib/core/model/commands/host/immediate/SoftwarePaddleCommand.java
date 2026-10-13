package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.CommandConfiguration;
import de.do9fse.winkey.lib.core.model.Validations;

@CommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2)
public record SoftwarePaddleCommand(PaddleSelection selection) implements HostModeCommand {
    public enum PaddleSelection {
        UP,
        DIT,
        DAH,
        BOTH
    }

    public SoftwarePaddleCommand {
        Validations.validateNotNull("Paddle selection", selection);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x14, (byte) selection.ordinal() };
    }
}
