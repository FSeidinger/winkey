package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;

class ApplicationStateMachineTest {
    private ApplicationContext context;

    // Subject under test
    private ApplicationStateMachine stateMachine;

    @BeforeEach
    void setUp() {
        this.context = new ApplicationContext(ProtocolVersion.V1);
        this.context.setDeviceConfiguration(SerialConfiguration.DEFAULT);
        this.stateMachine = new ApplicationStateMachine();
    }

    @Test
    void startsClosed() {
        assertEquals(ApplicationState.CLOSED, stateMachine.getState());
    }

    @Test
    void supportsNormalInitializationAndCloseTransitions() {
        stateMachine.transitionToState(context, ApplicationState.INITIALIZING);
        assertEquals(ApplicationState.INITIALIZING, stateMachine.getState());

        stateMachine.transitionToState(context, ApplicationState.STANDALONE_MODE);
        assertEquals(ApplicationState.STANDALONE_MODE, stateMachine.getState());

        stateMachine.transitionToState(context, ApplicationState.CLOSED);
        assertEquals(ApplicationState.CLOSED, stateMachine.getState());
    }

    @Test
    void allowsClosingWhenInitializationDoesNotComplete() {
        final ApplicationStateMachine stateMachine = new ApplicationStateMachine();
        stateMachine.transitionToState(context, ApplicationState.INITIALIZING);
        stateMachine.transitionToState(context, ApplicationState.CLOSED);

        assertEquals(ApplicationState.CLOSED, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromClosed() {
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, ApplicationState.CLOSED));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, ApplicationState.STANDALONE_MODE));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, ApplicationState.HOST_MODE));
        assertEquals(ApplicationState.CLOSED, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromInitializing() {
        stateMachine.transitionToState(context, ApplicationState.INITIALIZING);

        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, ApplicationState.INITIALIZING));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, ApplicationState.HOST_MODE));
        assertEquals(ApplicationState.INITIALIZING, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromStandaloneMode() {
        stateMachine.transitionToState(context, ApplicationState.INITIALIZING);
        stateMachine.transitionToState(context, ApplicationState.STANDALONE_MODE);

        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, ApplicationState.STANDALONE_MODE));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, ApplicationState.INITIALIZING));
        assertEquals(ApplicationState.STANDALONE_MODE, stateMachine.getState());
    }

    @Test
    void supportsTransitionsBetweenStandaloneAndHostModeAndClosingFromHostMode() {
        stateMachine.transitionToState(context, ApplicationState.INITIALIZING);
        stateMachine.transitionToState(context, ApplicationState.STANDALONE_MODE);

        stateMachine.transitionToState(context, ApplicationState.HOST_MODE);
        assertEquals(ApplicationState.HOST_MODE, stateMachine.getState());

        stateMachine.transitionToState(context, ApplicationState.STANDALONE_MODE);
        assertEquals(ApplicationState.STANDALONE_MODE, stateMachine.getState());

        stateMachine.transitionToState(context, ApplicationState.HOST_MODE);
        assertEquals(ApplicationState.HOST_MODE, stateMachine.getState());

        stateMachine.transitionToState(context, ApplicationState.CLOSED);
        assertEquals(ApplicationState.CLOSED, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromHostMode() {
        stateMachine.transitionToState(context, ApplicationState.INITIALIZING);
        stateMachine.transitionToState(context, ApplicationState.STANDALONE_MODE);
        stateMachine.transitionToState(context, ApplicationState.HOST_MODE);

        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, ApplicationState.HOST_MODE));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, ApplicationState.INITIALIZING));
        assertEquals(ApplicationState.HOST_MODE, stateMachine.getState());
    }
}
