package de.do9fse.winkey.lib.core.model.responses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StatusResponseTest {
    @Test
    void parsesWk1CompatibleStatusFields() {
        final StatusResponse response = StatusResponseWK1.fromData(new byte[] { (byte) 0xdf });

        assertTrue(response.waiting());
        assertTrue(response.keyDown());
        assertTrue(response.busy());
        assertTrue(response.breakIn());
        assertTrue(response.xoff());
        assertFalse(response.pushButtonStatus());
        assertEquals(0, response.pushButtonMask());
    }

    @Test
    void parsesWk2StatusAndPushButtonFieldsWhenConfigured() {
        final StatusResponse statusResponse = StatusResponseWK2.fromData(new byte[] { (byte) 0xd7 });
        assertTrue(statusResponse.waiting());
        assertFalse(statusResponse.keyDown());
        assertTrue(statusResponse.busy());
        assertTrue(statusResponse.breakIn());
        assertTrue(statusResponse.xoff());
        assertFalse(statusResponse.pushButtonStatus());

        final StatusResponse buttonResponse = StatusResponseWK2.fromData(new byte[] { (byte) 0xdf });
        assertFalse(buttonResponse.keyDown());
        assertFalse(buttonResponse.waiting());
        assertFalse(buttonResponse.busy());
        assertTrue(buttonResponse.pushButtonStatus());
        assertEquals(15, buttonResponse.pushButtonMask());
    }

    @Test
    void rejectsStatusBytesWithoutTheProtocolTag() {
        assertThrows(
            IllegalArgumentException.class,
            () -> StatusResponseWK1.fromData(new byte[] { 0x7f })
        );
    }
}
