package de.do9fse.winkey.lib.core.model.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class WinKeyRuntimeExceptionTest {
    @Test
    void storesMessage() {
        final WKRuntimeException exception = new WKRuntimeException("Runtime failure");

        assertEquals("Runtime failure", exception.getMessage());
    }

    @Test
    void storesMessageAndCause() {
        final Throwable cause = new IllegalStateException("Root cause");
        final WKRuntimeException exception = new WKRuntimeException("Runtime failure", cause);

        assertEquals("Runtime failure", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}
