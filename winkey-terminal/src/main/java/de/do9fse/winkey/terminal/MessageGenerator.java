package de.do9fse.winkey.terminal;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import de.do9fse.winkey.lib.core.model.ApplicationContext;

public class MessageGenerator {
    @FunctionalInterface
    public interface MessageFactory {
        String createErrorMessage(final ApplicationContext context);
    }

    private final Map<Errors, MessageFactory> messageFactories = new HashMap<>() {{
        put(Errors.NO_PORT_SET, MessageGenerator::noPortSet);
        put(Errors.INVALID_PORT, MessageGenerator::invalidPort);

        put(Errors.DEVICE_ALREADY_OPEN, MessageGenerator::deviceAlreadyOpen);
        put(Errors.DEVICE_ALREADY_CLOSED, MessageGenerator::deviceAlreadyClosed);
        put(Errors.DEVICE_DID_NOT_ANSWER, MessageGenerator::deviceDidNotAnswer);
        put(Errors.DEVICE_INITIALIZING_FAILED, MessageGenerator::deviceInitializingFailed);
    }};

    public String createErrorMessage(final Errors error, final ApplicationContext context) {
        final MessageFactory factory = this.messageFactories.get(error);
        return factory.createErrorMessage(context);
    }

    private static String noPortSet(final ApplicationContext context) {
        return "Error: you have to set a valid port name first";
    }
    
    private static String invalidPort(final ApplicationContext context) {
        return "Invalid port " + resolvePortName(context);
    }

    private static String deviceAlreadyOpen(final ApplicationContext context) {
        return "Device " + resolvePortName(context) + " is already open";
    }

    private static String deviceAlreadyClosed(final ApplicationContext context) {
        return "Device " + resolvePortName(context) + " is already closed";
    }

    private static String deviceDidNotAnswer(final ApplicationContext context) {
        return "Device " + resolvePortName(context) + " did not answer";
    }

    public static String successfullyOpenedDevice(final ApplicationContext context) {
        return "Successfully opened device " + resolvePortName(context);
    }

    public static String deviceInitializingFailed(final ApplicationContext context) {
        return "Failed to initialize device " + resolvePortName(context);
    }

    private static String resolvePortName(final ApplicationContext context) {
        try {
            return context.getPortName();
        } catch (final NoSuchElementException e) {
            return " <empty>";
        }
    }
}
