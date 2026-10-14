package de.do9fse.winkey.terminal.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;
import java.util.Locale;

import javax.measure.Quantity;
import javax.measure.quantity.Time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import de.do9fse.winkey.lib.core.model.commands.host.buffered.WaitCommand;
import de.do9fse.winkey.lib.core.model.data.DataItem;
import de.do9fse.winkey.terminal.test.tools.QuantityConverter;

@DisplayName("Given the data item parser")
public class ParserTest {
    private final Parser parserUS = new Parser();
    private final Parser parserDE = new Parser(Locale.GERMANY);

    @CsvSource({
        "@W10,              10 s", // Value without unit -> default unit of seconds implied
        "@W15.0,            15 s", // Decimal value without unit -> default unit of seconds implied
        "@W1000ms,           1 s", // Value of milli seconds get converted to seconds
        "@W2000000µs,        2 s", // Value of micro seconds get converted to seconds
        "@W3000000000ns,     3 s"  // Value of nano seconds get converted to seconds
    })
    @ParameterizedTest(name = "when parsing wait command \"{0}\", it should have a duration of {1}")
    void assertThatParsingWaitCommandSucceeds(
        final String line,
        
        @ConvertWith(QuantityConverter.class)
        final Quantity<Time> duration
    ) throws Exception {
        final List<DataItem> dataItems = parserUS.parse(line);

        assertThat(dataItems)
            .containsExactly(
                new WaitCommand(duration)
            );
    }

    @Test
    void assertParsingWithLocaleSucceeds() throws Exception{
        assertThatThrownBy(() -> parserDE.parse("@W1,7"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Buffered wait must be an integer between 0 and 99 seconds but was 1.7");
    }

    @Test
    void assertThatParsingAnInvalidCommandFails() throws Exception {
        assertThatThrownBy(() -> parserUS.parse("@Z1000"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Unknown command identifier 'Z'");
    }

    @CsvSource({
        "@W1.5s",
        "@W1500ms",
        "@W10V",
        "@W10Heinz",
        "@W10.235Heinz"
    })
    @ParameterizedTest(name = "when parsing the invalid wait command \"{0}\", it should fail")
    void assertThatParsingAnInvalidWaitCommandFails(
        final String line
    ) throws Exception {
        assertThatThrownBy(() -> parserUS.parse(line))
        .isInstanceOf(IllegalArgumentException.class)
        .message()
        .containsAnyOf(
            "Buffered wait must be an integer between 0 and 99 seconds but was 1.5",
            "Unit V is not compatible with Time",
            "Your unit Heinz is not a valid SI unit"
        );
    }
}
