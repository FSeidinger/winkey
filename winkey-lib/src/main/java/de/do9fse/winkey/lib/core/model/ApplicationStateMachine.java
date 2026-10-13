package de.do9fse.winkey.lib.core.model;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationStateMachine {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStateMachine.class);

    private ApplicationState state;

    public ApplicationStateMachine() {
        this.state = ApplicationState.CLOSED;
        LOGGER.info("Application is {}", this.state);
    }

    public ApplicationState getState() {
        return state;
    }

    public void transitionToState(final ApplicationContext context, final ApplicationState newState) {
        switch (state) {
            case CLOSED:
                switch (newState) {
                    case INITIALIZING:
                        Validations.validateNotNull("Device configuration", context.getDeviceConfiguration());
                        doTransition(newState);
                        break;

                    default: raiseStateTransitionError(this.state, newState);
                }
                break;

            case INITIALIZING:
                switch (newState) {
                    case CLOSED:
                    case READY:
                        doTransition(newState);
                        break;
                
                    default: raiseStateTransitionError(this.state, newState);
                }
                break;

            case READY:
                switch (newState) {
                    case CLOSED:
                        doTransition(newState);
                        break;
                
                    default: raiseStateTransitionError(this.state, newState);
                }
                break;
        }
    }

    private void doTransition(final ApplicationState newState) {
        this.state = newState;
        LOGGER.info("Application is now {}", newState);
    }

    private void raiseStateTransitionError(final ApplicationState oldState, final ApplicationState newState) {
        final String message = formatError(
            "Transitioning from state {0} to state{1} is not allowed",
            oldState,
            newState
        );

        throw new IllegalStateException(message);
    }

    private String formatError(final String pattern, final Object ... args) {
        return MessageFormat.format(pattern, args);
    }
}
