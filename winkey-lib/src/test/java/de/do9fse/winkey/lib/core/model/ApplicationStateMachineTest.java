package de.do9fse.winkey.lib.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.configuration.serial.DeviceConfiguration;

class ApplicationStateMachineTest {
    private ApplicationContext context;

    // Subject under test
    private ApplicationStateMachine stateMachine;

    @BeforeEach
    void setUp() {
        this.context = new ApplicationContext(WinKeyProtocolVersion.V1);
        this.context.setDeviceConfiguration(DeviceConfiguration.DEFAULT);
        this.stateMachine = new ApplicationStateMachine();
    }

    @Test
    void startsClosed() {
        assertEquals(WinKeyState.CLOSED, stateMachine.getState());
    }

    @Test
    void supportsNormalInitializationAndCloseTransitions() {
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);
        assertEquals(WinKeyState.INITIALIZING, stateMachine.getState());

        stateMachine.transitionToState(context, WinKeyState.READY);
        assertEquals(WinKeyState.READY, stateMachine.getState());

        stateMachine.transitionToState(context, WinKeyState.CLOSED);
        assertEquals(WinKeyState.CLOSED, stateMachine.getState());
    }

    @Test
    void allowsClosingWhenInitializationDoesNotComplete() {
        final ApplicationStateMachine stateMachine = new ApplicationStateMachine();
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);
        stateMachine.transitionToState(context, WinKeyState.CLOSED);

        assertEquals(WinKeyState.CLOSED, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromClosed() {
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.CLOSED));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.READY));
        assertEquals(WinKeyState.CLOSED, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromInitializing() {
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);

        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.INITIALIZING));
        assertEquals(WinKeyState.INITIALIZING, stateMachine.getState());
    }

    @Test
    void rejectsTransitionsNotAllowedFromReady() {
        stateMachine.transitionToState(context, WinKeyState.INITIALIZING);
        stateMachine.transitionToState(context, WinKeyState.READY);

        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.READY));
        assertThrows(IllegalStateException.class, () -> stateMachine.transitionToState(context, WinKeyState.INITIALIZING));
        assertEquals(WinKeyState.READY, stateMachine.getState());
    }
}
