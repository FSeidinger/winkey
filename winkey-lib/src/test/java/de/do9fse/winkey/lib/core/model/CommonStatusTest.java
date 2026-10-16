package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Common status tests")
class CommonStatusTest {

    @Test
    @DisplayName("Test all flags set")
    void parsesAllFlagsSet() {
        final CommonStatus status = CommonStatus.fromData(0b00010111);

        assertTrue(status.waitFlag());
        assertTrue(status.busyFlag());
        assertTrue(status.breakinFlag());
        assertTrue(status.xoffFlag());
    }

    @Test
    @DisplayName("Test all flags cleared")
    void parsesNoFlagsSet() {
        final CommonStatus status = CommonStatus.fromData(0);

        assertFalse(status.waitFlag());
        assertFalse(status.busyFlag());
        assertFalse(status.breakinFlag());
        assertFalse(status.xoffFlag());
    }

    @ParameterizedTest
    @CsvSource({
        "0b00010000, true, false, false, false",
        "0b00000100, false, true, false, false",
        "0b00000010, false, false, true, false",
        "0b00000001, false, false, false, true"
    })
    @DisplayName("Test individual flags parsing")
    void parsesIndividualFlags(
        final String binaryString,
        final boolean expectedWait,
        final boolean expectedBusy,
        final boolean expectedBreakin,
        final boolean expectedXoff
    ) {
        final int data = Integer.parseInt(binaryString.replace("0b", ""), 2);
        final CommonStatus status = CommonStatus.fromData(data);

        assertEquals(expectedWait, status.waitFlag());
        assertEquals(expectedBusy, status.busyFlag());
        assertEquals(expectedBreakin, status.breakinFlag());
        assertEquals(expectedXoff, status.xoffFlag());
    }
}