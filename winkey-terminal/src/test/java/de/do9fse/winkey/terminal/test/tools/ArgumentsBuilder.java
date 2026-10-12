package de.do9fse.winkey.terminal.test.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ArgumentsBuilder {
    public static final String[] EMPTY = new String[0];

    private final List<String> arguments = new ArrayList<>();

    private ArgumentsBuilder() {}

    public static ArgumentsBuilder builder() {
        return new ArgumentsBuilder();
    }

    public ArgumentsBuilder addOption(final String option) {
        Objects.requireNonNull(option, "Option must not be null");
        arguments.add(option);

        return this;
    }

    public ArgumentsBuilder addOption(final String option, final String value) {
        Objects.requireNonNull(option, "Option must not be null");
        arguments.add(option);

        Objects.requireNonNull(value, "Value must not be null");
        arguments.add(value);

        return this;
    }

    public ArgumentsBuilder addLongOption(final String option) {
        Objects.requireNonNull(option, "Option must not be null");
        arguments.add(option);
        return this;
    }

    public ArgumentsBuilder addLongOption(final String option, final String value) {
        Objects.requireNonNull(option, "Option must not be null");
        Objects.requireNonNull(value, "Value must not be null");

        arguments.add(option + "=" + value);

        return this;
    }

    public ArgumentsBuilder addArgument(final String argument) {
        Objects.requireNonNull(argument, "Argument must not be null");
        arguments.add(argument);
        
        return this;
    }

    public String[] build() {
        return arguments.toArray(String[]::new);
    }
}
