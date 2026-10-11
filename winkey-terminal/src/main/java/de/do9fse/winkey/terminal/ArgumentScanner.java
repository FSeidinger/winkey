package de.do9fse.winkey.terminal;

import java.util.NoSuchElementException;
import java.util.Objects;

public class ArgumentScanner {
    private final String[] arguments;
    private int position = 0;

    public ArgumentScanner(final String[] arguments) {
        this.arguments = Objects.requireNonNull(arguments, "Arguments must not be null");
    }

    public boolean hasNext() {
        return position < arguments.length;
    }

    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more arguments");
        }

        return arguments[position++];
    }
}
