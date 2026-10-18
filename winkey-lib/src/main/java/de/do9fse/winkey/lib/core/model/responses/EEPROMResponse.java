package de.do9fse.winkey.lib.core.model.responses;

import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.configuration.EEPROMContent;

@ResponseConfiguration(expectedResponseBytes = EEPROMContent.EEPROM_SIZE)
public record EEPROMResponse(EEPROMContent eeprom) implements Response {
    public EEPROMResponse {
        Validations.validateNotNull("EEPROM", eeprom);
    }

    public static EEPROMResponse fromData(final byte[] responseBytes) {
        return new EEPROMResponse(new EEPROMContent(responseBytes));
    }
}
