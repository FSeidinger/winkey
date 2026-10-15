package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProtocolVersionTest {

    @ParameterizedTest
    @CsvSource({
        "1, V1",
        "2, V2",
        "3, V3"
    })
    void mapsMajorVersionToProtocolVersion(final int majorVersion, final ProtocolVersion expected) {
        assertEquals(expected, ProtocolVersion.fromMajorVersion(majorVersion));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 4, -1 })
    void rejectsUnsupportedMajorVersion(final int majorVersion) {
        assertThrows(IllegalArgumentException.class, () -> ProtocolVersion.fromMajorVersion(majorVersion));
    }

    @Test
    void returnsProtocolMajorVersion() {
        assertEquals(1, ProtocolVersion.V1.getProtocolMajorVersion());
        assertEquals(2, ProtocolVersion.V2.getProtocolMajorVersion());
        assertEquals(3, ProtocolVersion.V3.getProtocolMajorVersion());
    }
}
