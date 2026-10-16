package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Status V1 tests")
class StatusV1Test {

    @Test
    @DisplayName("Test that all flags are parsed when set")
    void parsesAllFlagsSet() {
        final StatusV1 status = StatusV1.fromData(0xdf);

        assertTrue(status.keyDown());
        assertTrue(status.commonStatus().waitFlag());
        assertTrue(status.commonStatus().busyFlag());
        assertTrue(status.commonStatus().breakinFlag());
        assertTrue(status.commonStatus().xoffFlag());
    }

    @Test
    @DisplayName("Test that all flags are false when cleared")
    void parsesNoFlagsSet() {
        final StatusV1 status = StatusV1.fromData(0xc0);

        assertFalse(status.keyDown());
        assertFalse(status.commonStatus().waitFlag());
        assertFalse(status.commonStatus().busyFlag());
        assertFalse(status.commonStatus().breakinFlag());
        assertFalse(status.commonStatus().xoffFlag());
    }

    @ParameterizedTest
    @CsvSource({
        "0xc8, true, false, false, false, false",
        "0xd0, false, true, false, false, false",
        "0xc4, false, false, true, false, false",
        "0xc2, false, false, false, true, false",
        "0xc1, false, false, false, false, true"
    })
    @DisplayName("Test individual flags parsing")
    void parsesIndividualFlags(
        final int data,
        final boolean expectedKeyDown,
        final boolean expectedWait,
        final boolean expectedBusy,
        final boolean expectedBreakin,
        final boolean expectedXoff
    ) {
        final StatusV1 status = StatusV1.fromData(data);

        assertEquals(expectedKeyDown, status.keyDown());
        assertEquals(expectedWait, status.commonStatus().waitFlag());
        assertEquals(expectedBusy, status.commonStatus().busyFlag());
        assertEquals(expectedBreakin, status.commonStatus().breakinFlag());
        assertEquals(expectedXoff, status.commonStatus().xoffFlag());
    }

    @Test
    @DisplayName("Test record structure")
    void recordHoldsComponents() {
        final CommonStatus common = new CommonStatus(true, false, true, false);
        final StatusV1 status = new StatusV1(common, true);

        assertEquals(common, status.commonStatus());
        assertTrue(status.keyDown());
    }
}