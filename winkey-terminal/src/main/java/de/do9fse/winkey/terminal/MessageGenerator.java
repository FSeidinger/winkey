package de.do9fse.winkey.terminal;

import java.util.NoSuchElementException;

import de.do9fse.winkey.lib.core.model.WinKeyContext;

public class MessageGenerator {
    public String successfullyOpenedDevice(final WinKeyContext context) {
        return "Successfully opened device " + resolvePortName(context);
    }

    public String deviceInitializingFailed(final WinKeyContext context) {
        return "Failed to initialize device " + resolvePortName(context);
    }

    private String resolvePortName(final WinKeyContext context) {
        try {
            return context.getPortName();
        } catch (final NoSuchElementException e) {
            return " <empty>";
        }
    }
}
