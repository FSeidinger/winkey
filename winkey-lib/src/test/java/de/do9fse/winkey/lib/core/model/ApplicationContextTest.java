package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;

class ApplicationContextTest {
    // Subject under test
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        this.context = new ApplicationContext(ProtocolVersion.V2);
        this.context.setDeviceConfiguration(SerialConfiguration.DEFAULT);
    }

    @Test
    void retainsConfiguredProtocolVersionAndStartsClosed() {
        assertEquals(ProtocolVersion.V2, context.getVersion());
        assertEquals(ApplicationState.CLOSED, context.getState());
    }

    @Test
    void delegatesStateTransitionsToItsStateMachine() {
        context.transitionToState(ApplicationState.INITIALIZING);
        context.transitionToState(ApplicationState.READY);

        assertEquals(ApplicationState.READY, context.getState());
    }

    @Test
    void rejectsANullProtocolVersion() {
        assertThrows(NullPointerException.class, () -> new ApplicationContext(null));
    }
}
