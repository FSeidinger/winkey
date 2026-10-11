package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.configuration.serial.DeviceConfiguration;

class ApplicationContextTest {
    // Subject under test
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        this.context = new ApplicationContext(WinKeyProtocolVersion.V2);
        this.context.setDeviceConfiguration(DeviceConfiguration.DEFAULT);
    }

    @Test
    void retainsConfiguredProtocolVersionAndStartsClosed() {
        assertEquals(WinKeyProtocolVersion.V2, context.getVersion());
        assertEquals(WinKeyState.CLOSED, context.getState());
    }

    @Test
    void delegatesStateTransitionsToItsStateMachine() {
        context.transitionToState(WinKeyState.INITIALIZING);
        context.transitionToState(WinKeyState.READY);

        assertEquals(WinKeyState.READY, context.getState());
    }

    @Test
    void rejectsANullProtocolVersion() {
        assertThrows(NullPointerException.class, () -> new ApplicationContext(null));
    }
}
