package de.do9fse.winkey.lib.core.model.configuration;

import de.do9fse.winkey.lib.core.model.Validations;

public record EEPROMContent(byte[] content) {
    public static final int EEPROM_SIZE = 256;

    public EEPROMContent {
        Validations.validateNotNull("EEPROM data", content);

        if (content.length != EEPROM_SIZE) {       
            throw new IllegalArgumentException(
                "EEPROM content must contain exactly " + 
                EEPROM_SIZE + 
                " bytes but contained " +
                content.length
            );
        }
    }
}
