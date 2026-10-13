package de.do9fse.winkey.lib.core.model.responses;

import java.text.MessageFormat;
import java.util.Arrays;
import de.do9fse.winkey.lib.core.model.Validations;

@ResponseConfiguration(expectedResponseBytes = EEPROMDumpResponse.DATA_LENGTH)
public record EEPROMDumpResponse(byte[] data) implements Response {
    public static final int DATA_LENGTH = 256;

    public EEPROMDumpResponse {
        Validations.validateNotNull("EEPROM dump data", data);

        if (data.length != DATA_LENGTH) {
            final String message = MessageFormat.format(
                "EEPROM dump must contain exactly {0} bytes but contained {1}",
                DATA_LENGTH,
                data.length
            );
        
            throw new IllegalArgumentException(message);
        }
        
        data = Arrays.copyOf(data, data.length);
    }

    @Override
    public byte[] data() {
        return Arrays.copyOf(data, data.length);
    }

    public static EEPROMDumpResponse fromData(final byte[] responseBytes) {
        return new EEPROMDumpResponse(responseBytes);
    }
}
