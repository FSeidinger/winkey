package de.do9fse.winkey.terminal;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentScanner {
    public record CommandOption(String option, String value, boolean isLongOption) {
        public boolean hasOption() {
            return option != null;
        }

        public boolean hasValue() {
            return value != null;
        }

        public boolean isShortOption() {
            return !isLongOption;
        }
    }

    public static final String OPTION_GROUP = "option";
    public static final String VALUE_GROUP = "value";

    public static final Pattern OPTION_PATTERN = Pattern.compile("(?<" + OPTION_GROUP + ">(-|--)\\w+)(?:=|[ \\t]+)?(?<" + VALUE_GROUP + ">\\w+)?");

    private final String[] arguments;
    private int position = 0;

    public ArgumentScanner(final String[] arguments) {
        this.arguments = Objects.requireNonNull(arguments, "Arguments must not be null");
    }

    public boolean hasNext() {
        return hasNNext(this.position);
    }

    public String next() throws NoSuchElementException {
        if (hasNext()) {
            return arguments[position++];
        }

        throw new NoSuchElementException("No more arguments");
    }

    public CommandOption nextOption() throws NoSuchElementException {
        final CommandOption nextOption = peekOption(true);

        // Consume option
        next();


        // Consume value if it is a short option
        if (nextOption.isShortOption() && nextOption.value() != null) {
            next();
        }

        return nextOption;
    }

    public String peek() throws NoSuchElementException {
        return peek(this.position);
    }

    public CommandOption peekOption() throws NoSuchElementException, IllegalArgumentException {
        return this.peekOption(false);
    }

    private boolean hasNNext(final int position) {
        return position < arguments.length;
    }
    
    private String peek(final int position) throws NoSuchElementException {
        if (position < arguments.length) {
            return arguments[position];
        }

        throw new NoSuchElementException("No more arguments");
    }


    private CommandOption peekOption(final boolean raiseError) throws NoSuchElementException, IllegalArgumentException {
        try {
            final String optionWithArgument = peekOptionWithArgument();
            final Matcher optionMatcher = OPTION_PATTERN.matcher(optionWithArgument);

            if (!optionMatcher.matches()) {
                throw new IllegalArgumentException("Next argument is not an option");
            }

            final String optionGroup = optionMatcher.group(OPTION_GROUP);
            final String valueGroup = optionMatcher.group(VALUE_GROUP);
            final boolean isLongOption = optionWithArgument.startsWith("--");

            return new CommandOption(optionGroup, valueGroup, isLongOption);
        } catch(final NoSuchElementException | IllegalArgumentException e) {
            if (raiseError) {
                throw e;
            }

            return null;
        }
    }

    private String peekOptionWithArgument() {
        final String option = peek(position);
        final boolean isLongOption = option.startsWith("--");

        if (!isLongOption) {
            final String value = tryReadShortOptionValue(position + 1);
            if (value != null) {
                return String.join(" ", option, value);
            }
        }

        return option;
    }

    private String tryReadShortOptionValue(final int position) {
        try {
            return peek(position);
        } catch(final NoSuchElementException e) {
            return null;
        }
    }
}
