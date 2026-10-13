package de.do9fse.winkey.terminal;

import java.util.NoSuchElementException;

import de.do9fse.winkey.lib.core.model.ApplicationContext;

public enum Errors {
    NO_PORT_SET,
    INVALID_PORT,

    APPLICATION_MUST_BE_READY,

    DEVICE_ALREADY_OPEN,
    DEVICE_ALREADY_CLOSED,
    DEVICE_DID_NOT_ANSWER,
    DEVICE_INITIALIZING_FAILED;

    public String createErrorMessage(final ApplicationContext context) {
        switch (this) {
            case NO_PORT_SET: return "You have to set a valid port name first";
            case INVALID_PORT: return "Invalid port " + resolvePortName(context);

            case APPLICATION_MUST_BE_READY: return "Device is closed. Please call 'connect' first.";
            
            case DEVICE_ALREADY_OPEN: return "Device " + resolvePortName(context) + " is already open";
            case DEVICE_ALREADY_CLOSED: return "Device " + resolvePortName(context) + " is already closed";
            case DEVICE_DID_NOT_ANSWER: return "Device " + resolvePortName(context) + " did not answer";
            case DEVICE_INITIALIZING_FAILED: return "Failed to initialize device " + resolvePortName(context);

            default: throw new IllegalArgumentException("Unknown error message" + this);
        }
    }

    private String resolvePortName(final ApplicationContext context) {
        try {
            return context.getPortName();
        } catch (final NoSuchElementException e) {
            return "<unknown>";
        }
    }
}
