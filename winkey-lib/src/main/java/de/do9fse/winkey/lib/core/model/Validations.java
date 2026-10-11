package de.do9fse.winkey.lib.core.model;

import java.util.Objects;

public class Validations {
    public static <T> T validateNotNull(final String fieldName, final T value) {
        return Objects.requireNonNull(value, fieldName + " must not be null");
    }

    public static String validateNotBlank(final String fieldName, final String value) {
        validateNotNull(fieldName, value);

        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        return value;
    }
}
