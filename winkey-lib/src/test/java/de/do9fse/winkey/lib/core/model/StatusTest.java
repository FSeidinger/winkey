package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Status interface tests")
class StatusTest {

    @Test
    @DisplayName("Test fromData for protocol version V1 returns StatusV1")
    void fromDataV1ReturnsStatusV1() {
        final Status status = Status.fromData(0xdf, ProtocolVersion.V1);
        assertInstanceOf(StatusV1.class, status);
    }

    @Test
    @DisplayName("Test fromData for protocol version V2 without push button returns StatusV2")
    void fromDataV2WithoutPushButtonReturnsStatusV2() {
        final Status status = Status.fromData(0xd7, ProtocolVersion.V2);
        assertInstanceOf(StatusV2.class, status);
    }

    @Test
    @DisplayName("Test fromData for protocol version V2 with push button returns StatusPushButton")
    void fromDataV2WithPushButtonReturnsStatusPushButton() {
        final Status status = Status.fromData(0xdf, ProtocolVersion.V2);
        assertInstanceOf(StatusPushButton.class, status);
    }

    @Test
    @DisplayName("Test fromData rejects unsupported protocol version")
    void fromDataRejectsUnsupportedProtocolVersion() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Status.fromData(0xdf, ProtocolVersion.V3)
        );
    }

    @Test
    @DisplayName("Test filter method")
    void filterChecksMask() {
        assertTrue(Status.filter(0b11001000, Status.MASK_PUSH_BUTTON));
        assertFalse(Status.filter(0b11000000, Status.MASK_PUSH_BUTTON));
    }
}