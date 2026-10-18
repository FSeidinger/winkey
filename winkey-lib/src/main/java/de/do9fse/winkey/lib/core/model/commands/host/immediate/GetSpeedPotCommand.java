package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.responses.SpeedPotValueResponse;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2,
    responseType = SpeedPotValueResponse.class
)
public record GetSpeedPotCommand() implements ImmediateCommand {
    @Override
    public byte[] toData() {
        return new byte[] { 0x07 };
    }
}
