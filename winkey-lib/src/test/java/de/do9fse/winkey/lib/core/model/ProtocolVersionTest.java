package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Protocol version tests")
class ProtocolVersionTest {

    @ParameterizedTest
    @CsvSource({
        "V1, 1",
        "V2, 2",
        "V3, 3"
    })
    @DisplayName("Test that protocol version is converted to device version")
    void convertsToDeviceVersion(final ProtocolVersion version, final int expectedMajorVersion) {
        final DeviceVersion deviceVersion = version.toDeviceVersion();
        assertEquals(expectedMajorVersion, deviceVersion.majorVersion());
        assertEquals(DeviceVersion.MINOR_VERSION_MIN, deviceVersion.minorVersion());
    }
}
