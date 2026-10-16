package de.do9fse.winkey.lib.core.model.responses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.DeviceVersion;

@DisplayName("Version response tests")
class VersionResponseTest {

    @Test
    @DisplayName("Test that valid device version is accepted")
    void acceptsValidDeviceVersion() {
        final DeviceVersion deviceVersion = new DeviceVersion(2, 3);
        final VersionResponse response = new VersionResponse(deviceVersion);
        assertEquals(deviceVersion, response.version());
    }

    @Test
    @DisplayName("Test that null device version is rejected")
    void rejectsNullDeviceVersion() {
        assertThrows(NullPointerException.class, () -> new VersionResponse(null));
    }

    @Test
    @DisplayName("Test that response byte can be parsed")
    void parsesResponseByte() {
        final VersionResponse response = VersionResponse.fromData(new byte[] { 23 });

        assertEquals(new DeviceVersion(2, 3), response.version());
    }
}
