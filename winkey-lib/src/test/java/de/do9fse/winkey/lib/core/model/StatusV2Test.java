package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Status V2 tests")
class StatusV2Test {

    @Test
    @DisplayName("Test that all common flags are parsed when set")
    void parsesAllFlagsSet() {
        final StatusV2 status = StatusV2.fromData(0xd7);

        assertTrue(status.commonStatus().waitFlag());
        assertTrue(status.commonStatus().busyFlag());
        assertTrue(status.commonStatus().breakinFlag());
        assertTrue(status.commonStatus().xoffFlag());
    }

    @Test
    @DisplayName("Test that all flags are false when cleared")
    void parsesNoFlagsSet() {
        final StatusV2 status = StatusV2.fromData(0xc0);

        assertFalse(status.commonStatus().waitFlag());
        assertFalse(status.commonStatus().busyFlag());
        assertFalse(status.commonStatus().breakinFlag());
        assertFalse(status.commonStatus().xoffFlag());
    }

    @ParameterizedTest
    @CsvSource({
        "0xd0, true, false, false, false",
        "0xc4, false, true, false, false",
        "0xc2, false, false, true, false",
        "0xc1, false, false, false, true"
    })
    @DisplayName("Test individual flags parsing")
    void parsesIndividualFlags(
        final int data,
        final boolean expectedWait,
        final boolean expectedBusy,
        final boolean expectedBreakin,
        final boolean expectedXoff
    ) {
        final StatusV2 status = StatusV2.fromData(data);

        assertEquals(expectedWait, status.commonStatus().waitFlag());
        assertEquals(expectedBusy, status.commonStatus().busyFlag());
        assertEquals(expectedBreakin, status.commonStatus().breakinFlag());
        assertEquals(expectedXoff, status.commonStatus().xoffFlag());
    }

    @Test
    @DisplayName("Test record structure")
    void recordHoldsComponents() {
        final CommonStatus common = new CommonStatus(false, true, false, true);
        final StatusV2 status = new StatusV2(common);

        assertEquals(common, status.commonStatus());
    }
}