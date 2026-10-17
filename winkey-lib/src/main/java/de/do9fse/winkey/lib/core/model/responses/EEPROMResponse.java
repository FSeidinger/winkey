package de.do9fse.winkey.lib.core.model.responses;

import java.text.MessageFormat;

import de.do9fse.winkey.lib.core.model.Validations;

@ResponseConfiguration(expectedResponseBytes = EEPROMResponse.DATA_LENGTH)
public record EEPROMResponse(byte[] data) implements Response {
    public static final int DATA_LENGTH = 256;

    public EEPROMResponse {
        Validations.validateNotNull("EEPROM dump data", data);

        if (data.length != DATA_LENGTH) {
            final String message = MessageFormat.format(
                "EEPROM dump must contain exactly {0} bytes but contained {1}",
                DATA_LENGTH,
                data.length
            );
        
            throw new IllegalArgumentException(message);
        }
    }

    public static EEPROMResponse fromData(final byte[] responseBytes) {
        return new EEPROMResponse(responseBytes);
    }
}
