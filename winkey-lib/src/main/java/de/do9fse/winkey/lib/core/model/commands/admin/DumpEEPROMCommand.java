package de.do9fse.winkey.lib.core.model.commands.admin;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.responses.EEPROMDumpResponse;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2,
    responseType = EEPROMDumpResponse.class
)
public record  DumpEEPROMCommand() implements AdminCommand {
    @Override
    public byte[] toData() {
        return new byte[]{ 0x00, 0x0C };
    }
}
