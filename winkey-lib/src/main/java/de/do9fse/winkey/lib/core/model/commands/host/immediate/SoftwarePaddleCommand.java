package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
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
    public byte[] toData() {
        return new byte[] { 0x14, (byte) selection.ordinal() };
    }
}
