package de.do9fse.winkey.lib.core.model.error;

public class WKRuntimeException extends RuntimeException {
    public WKRuntimeException(final String message) {
        super(message);
    }

    public WKRuntimeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
