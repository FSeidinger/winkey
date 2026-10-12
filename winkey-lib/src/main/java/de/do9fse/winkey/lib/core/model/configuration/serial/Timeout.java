package de.do9fse.winkey.lib.core.model.configuration.serial;

import java.util.concurrent.TimeUnit;

import de.do9fse.winkey.lib.core.model.Validations;

public record Timeout(int timeout, TimeUnit timeUnit) {
    public Timeout {
        Validations.validateGreaterThanZero("Timeout", timeout);
        Validations.validateNotNull("Time unit", timeUnit);
    }

    public static final Timeout DEFAULT_READ_TIMEOUT = new Timeout(500, TimeUnit.MILLISECONDS);
    public static final Timeout DEFAULT_INITIALIZATION_TIMEOUT = new Timeout(10, TimeUnit.SECONDS);
}
