package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;

class WinKeyContextTest {
    // Subject under test
    private WinKeyContext context;

    @BeforeEach
    void setUp() {
        this.context = new WinKeyContext(ProtocolVersion.V2);
        this.context.setDeviceConfiguration(SerialConfiguration.DEFAULT);
    }

    @Test
    void retainsConfiguredProtocolVersionAndStartsClosed() {
        assertEquals(ProtocolVersion.V2, context.getProtocolVersion());
        assertEquals(new DeviceVersion(2, 0), context.getDeviceVersion());
        assertEquals(WinKeyState.CLOSED, context.getState());
    }

    @Test
    void delegatesStateTransitionsToItsStateMachine() {
        context.transitionToState(WinKeyState.INITIALIZING);
        context.transitionToState(WinKeyState.STANDALONE_MODE);
        assertEquals(WinKeyState.STANDALONE_MODE, context.getState());

        context.transitionToState(WinKeyState.HOST_MODE);
        assertEquals(WinKeyState.HOST_MODE, context.getState());
    }

    @Test
    void rejectsANullProtocolVersion() {
        assertThrows(NullPointerException.class, () -> new WinKeyContext(null));
    }

    @Test
    void setsAndGetsDeviceVersion() {
        final DeviceVersion newVersion = new DeviceVersion(3, 1);
        context.setDeviceVersion(newVersion);
        assertEquals(newVersion, context.getDeviceVersion());
        assertEquals(ProtocolVersion.V3, context.getProtocolVersion());
    }

    @Test
    void rejectsNullDeviceVersion() {
        assertThrows(NullPointerException.class, () -> context.setDeviceVersion(null));
    }
}
