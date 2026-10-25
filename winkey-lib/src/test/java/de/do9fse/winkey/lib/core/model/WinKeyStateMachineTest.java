package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;

class WinKeyStateMachineTest {
    private WinKeyContext context;

    // Subject under test
    private WinKeyStateMachine stateMachine;

    @BeforeEach
    void setUp() {
        this.context = new WinKeyContext(ProtocolVersion.V1);
        this.context.setDeviceConfiguration(SerialConfiguration.DEFAULT);
        this.stateMachine = new WinKeyStateMachine();
    }

    @Test
    void startsClosed() {
        assertEquals(WinKeyState.CLOSED, stateMachine.getState());
    }

    @Test
    void supportsNormalInitializationAndCloseTransitions() {
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);
        assertEquals(WinKeyState.INITIALIZING, stateMachine.getState());

        stateMachine.transitionToState(context, WinKeyState.STANDALONE_MODE);
        assertEquals(WinKeyState.STANDALONE_MODE, stateMachine.getState());

        stateMachine.transitionToState(context, WinKeyState.CLOSED);
        assertEquals(WinKeyState.CLOSED, stateMachine.getState());
    }

    @Test
    void allowsClosingWhenInitializationDoesNotComplete() {
        final WinKeyStateMachine stateMachine = new WinKeyStateMachine();
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);
        stateMachine.transitionToState(context, WinKeyState.CLOSED);

        assertEquals(WinKeyState.CLOSED, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromClosed() {
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.CLOSED));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.STANDALONE_MODE));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.HOST_MODE));
        assertEquals(WinKeyState.CLOSED, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromInitializing() {
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);

        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.INITIALIZING));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.HOST_MODE));
        assertEquals(WinKeyState.INITIALIZING, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromStandaloneMode() {
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);
        stateMachine.transitionToState(context, WinKeyState.STANDALONE_MODE);

        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.STANDALONE_MODE));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.INITIALIZING));
        assertEquals(WinKeyState.STANDALONE_MODE, stateMachine.getState());
    }

    @Test
    void supportsTransitionsBetweenStandaloneAndHostModeAndClosingFromHostMode() {
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);
        stateMachine.transitionToState(context, WinKeyState.STANDALONE_MODE);

        stateMachine.transitionToState(context, WinKeyState.HOST_MODE);
        assertEquals(WinKeyState.HOST_MODE, stateMachine.getState());

        stateMachine.transitionToState(context, WinKeyState.STANDALONE_MODE);
        assertEquals(WinKeyState.STANDALONE_MODE, stateMachine.getState());

        stateMachine.transitionToState(context, WinKeyState.HOST_MODE);
        assertEquals(WinKeyState.HOST_MODE, stateMachine.getState());

        stateMachine.transitionToState(context, WinKeyState.CLOSED);
        assertEquals(WinKeyState.CLOSED, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromHostMode() {
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);
        stateMachine.transitionToState(context, WinKeyState.STANDALONE_MODE);
        stateMachine.transitionToState(context, WinKeyState.HOST_MODE);

        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.HOST_MODE));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.INITIALIZING));
        assertEquals(WinKeyState.HOST_MODE, stateMachine.getState());
    }
}
