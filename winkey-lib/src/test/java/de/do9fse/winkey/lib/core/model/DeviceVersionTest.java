package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Device version tests")
class DeviceVersionTest {

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    @DisplayName("Test that valid major version is accepted")
    void acceptsValidMajorVersion(final int majorVersion) {
        final DeviceVersion version = new DeviceVersion(majorVersion, 0);
        assertEquals(majorVersion, version.majorVersion());
        assertEquals(0, version.minorVersion());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 4, -1 })
    @DisplayName("Test that invalid major version is rejected")
    void rejectsInvalidMajorVersion(final int majorVersion) {
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DeviceVersion(majorVersion, 0)
        );

        assertEquals(
            "WinKey major version must be between "
                + DeviceVersion.MAJOR_VERSION_MIN
                + " and "
                + DeviceVersion.MAJOR_VERSION_MAX
                + " but was "
                + majorVersion,
            exception.getMessage()
        );
    }

    @Test
    @DisplayName("Test that negative minor version is rejected")
    void rejectsNegativeMinorVersion() {
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DeviceVersion(2, -1)
        );

        assertEquals(
            "Minor version must not be less than "
                + DeviceVersion.MINOR_VERSION_MIN
                + " but was -1",
            exception.getMessage()
        );
    }

    @Test
    @DisplayName("Test that version string is generated")
    void generatesVersionString() {
        final DeviceVersion version = new DeviceVersion(2, 3);
        assertEquals("v2.3", version.getVersionString());
    }

    @ParameterizedTest
    @CsvSource({
        "2.1, 2, 1",
        "v2.1, 2, 1",
        "V3.0, 3, 0",
        "1.99, 1, 99"
    })
    @DisplayName("Test that version can be parsed")
    void parsesMajorAndMinorVersion(final String input, final int expectedMajor, final int expectedMinor) {
        final DeviceVersion version = DeviceVersion.parse(input);

        assertEquals(expectedMajor, version.majorVersion());
        assertEquals(expectedMinor, version.minorVersion());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "2",
        ".1",
        "2.",
        "x.1",
        "2.x",
        "4.0",
        "2.-1"
    })
    @DisplayName("Test that malformed or unsupported version strings are rejected")
    void rejectsInvalidVersionStrings(final String value) {
        assertThrows(IllegalArgumentException.class, () -> DeviceVersion.parse(value));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Test that null version strings are rejected")
    void rejectsNullVersionString(final String value) {
        assertThrows(NullPointerException.class, () -> DeviceVersion.parse(value));
    }

    @Test
    @DisplayName("Test mapping to protocol version")
    void mapsToProtocolVersion() {
        assertEquals(ProtocolVersion.V1, new DeviceVersion(1, 2).toProtocolVersion());
        assertEquals(ProtocolVersion.V2, new DeviceVersion(2, 3).toProtocolVersion());
        assertEquals(ProtocolVersion.V3, new DeviceVersion(3, 1).toProtocolVersion());
    }

    @Test
    @DisplayName("Test mapping from protocol version")
    void mapsFromProtocolVersion() {
        assertEquals(new DeviceVersion(1, DeviceVersion.MINOR_VERSION_MIN), DeviceVersion.fromProtocolVersion(ProtocolVersion.V1));
        assertEquals(new DeviceVersion(2, DeviceVersion.MINOR_VERSION_MIN), DeviceVersion.fromProtocolVersion(ProtocolVersion.V2));
        assertEquals(new DeviceVersion(3, DeviceVersion.MINOR_VERSION_MIN), DeviceVersion.fromProtocolVersion(ProtocolVersion.V3));
    }
}