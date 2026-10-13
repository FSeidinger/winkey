package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.DitDahRatio;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record SetDitDahRatioCommand(DitDahRatio ratio) implements ImmediateCommand {
    public SetDitDahRatioCommand {
        Validations.validateNotNull("Dit/dah ratio", ratio);
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x17, (byte) ratio.toProtocolValue() };
    }
}
