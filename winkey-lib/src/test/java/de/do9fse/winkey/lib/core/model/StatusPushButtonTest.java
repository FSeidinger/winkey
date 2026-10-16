package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Status push button tests")
class StatusPushButtonTest {

    @Test
    @DisplayName("Test that all push buttons are parsed when set")
    void parsesAllButtonsPressed() {
        final StatusPushButton status = StatusPushButton.fromData(0xdf);

        assertTrue(status.pb1Status());
        assertTrue(status.pb2Status());
        assertTrue(status.pb3Status());
        assertTrue(status.pb4Status());
    }

    @Test
    @DisplayName("Test that no buttons are parsed when cleared")
    void parsesNoButtonsPressed() {
        final StatusPushButton status = StatusPushButton.fromData(0xc8);

        assertFalse(status.pb1Status());
        assertFalse(status.pb2Status());
        assertFalse(status.pb3Status());
        assertFalse(status.pb4Status());
    }

    @ParameterizedTest
    @CsvSource({
        "0xc9, true, false, false, false",
        "0xca, false, true, false, false",
        "0xcc, false, false, true, false",
        "0xd8, false, false, false, true"
    })
    @DisplayName("Test individual button parsing")
    void parsesIndividualButtons(
        final int data,
        final boolean expectedPb1,
        final boolean expectedPb2,
        final boolean expectedPb3,
        final boolean expectedPb4
    ) {
        final StatusPushButton status = StatusPushButton.fromData(data);

        assertEquals(expectedPb1, status.pb1Status());
        assertEquals(expectedPb2, status.pb2Status());
        assertEquals(expectedPb3, status.pb3Status());
        assertEquals(expectedPb4, status.pb4Status());
    }

    @Test
    @DisplayName("Test record structure")
    void recordHoldsComponents() {
        final StatusPushButton status = new StatusPushButton(true, false, true, false);

        assertTrue(status.pb1Status());
        assertFalse(status.pb2Status());
        assertTrue(status.pb3Status());
        assertFalse(status.pb4Status());
    }
}