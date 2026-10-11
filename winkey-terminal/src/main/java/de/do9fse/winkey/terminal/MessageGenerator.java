package de.do9fse.winkey.terminal;

import java.util.NoSuchElementException;

import de.do9fse.winkey.lib.core.model.ApplicationContext;

public class MessageGenerator {
    public String successfullyOpenedDevice(final ApplicationContext context) {
        return "Successfully opened device " + resolvePortName(context);
    }

    public String deviceInitializingFailed(final ApplicationContext context) {
        return "Failed to initialize device " + resolvePortName(context);
    }

    private String resolvePortName(final ApplicationContext context) {
        try {
            return context.getPortName();
        } catch (final NoSuchElementException e) {
            return " <empty>";
        }
    }
}
