package de.do9fse.winkey.lib.core.model.commands.admin;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.AdminCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.EEPROMContent;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2
)
public record LoadEEPROMCommand(EEPROMContent eeprom) implements AdminCommand {
    public  LoadEEPROMCommand {
        Validations.validateNotNull("EEPROM", eeprom);
    }
    
    @Override
    public byte[] toData() {
        final byte[] requestBytes = new byte[eeprom.content().length + 2];

        requestBytes[0] = 0x00;
        requestBytes[1] = 0x0D;
        System.arraycopy(eeprom.content(), 0, requestBytes, 2, eeprom.content().length);

        return  requestBytes;
    }
}
