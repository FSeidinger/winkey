package de.do9fse.winkey.lib.core.model.responses;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EEPROM dump response tests")
class EEPROMDumpResponseTest {

    @Test
    void parses256ResponseBytes() {
        final byte[] responseBytes = new byte[256];
        responseBytes[0] = 0x12;
        responseBytes[255] = (byte) 0xff;

        final EEPROMResponse response = EEPROMResponse.fromData(responseBytes);

        assertArrayEquals(responseBytes, response.data());
    }

    @Test
    void rejectsDataWithIncorrectLength() {
        final byte[] responseBytes = new byte[255];

        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> EEPROMResponse.fromData(responseBytes)
        );

        assertEquals(
            "EEPROM dump must contain exactly " + EEPROMResponse.DATA_LENGTH + " bytes but contained 255",
            exception.getMessage()
        );
    }

    @Test
    void rejectsNullData() {
        final NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> new EEPROMResponse(null)
        );

        assertEquals("EEPROM dump data must not be null", exception.getMessage());
    }
}
