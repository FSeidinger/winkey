package de.do9fse.winkey.terminal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import de.do9fse.winkey.terminal.ArgumentScanner.CommandOption;
import de.do9fse.winkey.terminal.test.tools.ArgumentsBuilder;

public class ArgumentScannerTest {
    @Test
    void assertThatParsingEmptyArgumentLineSucceeds() {
        final String[] arguments = ArgumentsBuilder.EMPTY;
        final ArgumentScanner scanner = new ArgumentScanner(arguments);

        assertNoMoreArguments(scanner);
    }

    @Test
    void assertThatScanningAnArgumentSucceeds() {
        final String expectedArgument = "Heinz";

        final String[] arguments = ArgumentsBuilder
            .builder()
            .addArgument(expectedArgument)
            .build();
        final ArgumentScanner scanner = new ArgumentScanner(arguments);

        assertScanNextArgument(scanner, expectedArgument);
        assertNoMoreArguments(scanner);
    }

    @Test
    void assertThatScanningAShortOptionSucceeds() {
        final String expectedOption = "-d";
        final String expectedValue = null;

        final String[] arguments = ArgumentsBuilder
            .builder()
            .addOption(expectedOption)
            .build();
        final ArgumentScanner scanner = new ArgumentScanner(arguments);

        final CommandOption expectedCommandOption = new CommandOption(expectedOption, expectedValue, false);
        assertCanScanOption(scanner, expectedCommandOption);

        assertNoMoreArguments(scanner);
    }

    @Test
    void assertThatScanningAShortOptionWithValueSucceeds() {
        final String expectedOption = "-d";
        final String expectedValue = "true";

        final String[] arguments = ArgumentsBuilder
            .builder()
            .addOption(expectedOption, expectedValue)
            .build();
        final ArgumentScanner scanner = new ArgumentScanner(arguments);

        final CommandOption expectedCommandOption = new CommandOption(expectedOption, expectedValue, false);
        assertCanScanOption(scanner, expectedCommandOption);

        assertNoMoreArguments(scanner);
    }

    @Test
    void assertThatScanningALongOptionSucceeds() {
        final String expectedOption = "--doSomething";
        final String expectedValue = null;

        final String[] arguments = ArgumentsBuilder
            .builder()
            .addLongOption(expectedOption)
            .build();
        final ArgumentScanner scanner = new ArgumentScanner(arguments);

        final CommandOption expectedCommandOption = new CommandOption(expectedOption, expectedValue, true);
        assertCanScanOption(scanner, expectedCommandOption);

        assertNoMoreArguments(scanner);
    }

    @Test
    void assertThatScanningALongOptionWithValueSucceeds() {
        final String expectedOption = "--doSomething";
        final String expectedValue = "true";

        final String[] arguments = ArgumentsBuilder
            .builder()
            .addLongOption(expectedOption, expectedValue)
            .build();
        final ArgumentScanner scanner = new ArgumentScanner(arguments);

        final CommandOption expectedCommandOption = new CommandOption(expectedOption, expectedValue, true);
        assertCanScanOption(scanner, expectedCommandOption);

        assertNoMoreArguments(scanner);
    }

    private void assertCanScanOption(final ArgumentScanner scanner, final CommandOption expected) {
        // Peek
        assertEquals(expected, scanner.peekOption());

        // And consume
        assertEquals(expected, scanner.nextOption());
    }

    private void assertScanNextArgument(final ArgumentScanner scanner, final String argument) {
        assertTrue(scanner.hasNext());
        assertEquals(argument, scanner.peek());

        assertNull(scanner.peekOption());

        assertThatThrownBy(() -> scanner.nextOption())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Next argument is not an option");

        // Consume argument
        assertEquals(argument, scanner.next());
    }

    private void assertNoMoreArguments(final ArgumentScanner scanner) {
        assertFalse(scanner.hasNext());

        assertThatThrownBy(() -> scanner.next())
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("No more arguments");


        assertThatThrownBy(() -> scanner.peek())
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("No more arguments");


        assertNull(scanner.peekOption());

        assertThatThrownBy(() -> scanner.nextOption())
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("No more arguments");
    }
}
