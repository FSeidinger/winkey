package de.do9fse.winkey.terminal.test.tools;

import java.util.ArrayList;
import java.util.List;
import de.do9fse.winkey.lib.core.model.Validations;

public final class ArgumentsBuilder {
    public static final String[] EMPTY = new String[0];

    private final List<String> arguments = new ArrayList<>();

    private ArgumentsBuilder() {}

    public static ArgumentsBuilder builder() {
        return new ArgumentsBuilder();
    }

    public ArgumentsBuilder addOption(final String option) {
        Validations.validateNotNull("Option", option);
        arguments.add(option);

        return this;
    }

    public ArgumentsBuilder addOption(final String option, final String value) {
        Validations.validateNotNull("Option", option);
        arguments.add(option);

        Validations.validateNotNull("Value", value);
        arguments.add(value);

        return this;
    }

    public ArgumentsBuilder addLongOption(final String option) {
        Validations.validateNotNull("Option", option);
        arguments.add(option);
        return this;
    }

    public ArgumentsBuilder addLongOption(final String option, final String value) {
        Validations.validateNotNull("Option", option);
        Validations.validateNotNull("Value", value);

        arguments.add(option + "=" + value);

        return this;
    }

    public ArgumentsBuilder addArgument(final String argument) {
        Validations.validateNotNull("Argument", argument);
        arguments.add(argument);
        
        return this;
    }

    public String[] build() {
        return arguments.toArray(String[]::new);
    }
}
