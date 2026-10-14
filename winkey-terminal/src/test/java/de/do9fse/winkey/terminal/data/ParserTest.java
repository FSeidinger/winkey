package de.do9fse.winkey.terminal.data;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.do9fse.winkey.lib.core.model.data.DataItem;

@DisplayName("Given the data item parser")
public class ParserTest {
    private Parser parser;

    @BeforeEach
    void setUp() {
        this.parser = new Parser();
    }

    @CsvSource({
        "@C,        CancelBufferedSpeedCommand[]",
        "@L10,      HscwSpeedChangeCommand[lpm=1000 Hz/60]",
        "@N,        NopCommand[]",
        "@PTrue,    PttCommand[enabled=true]",
        "@POn,      PttCommand[enabled=true]",
        "@P1,       PttCommand[enabled=true]",
        "@PYes,     PttCommand[enabled=true]",
        "@Penabled, PttCommand[enabled=true]",
        "@S20,      ChangeSpeedBufferedCommand[wpm=20 WPM]",
        "@W10,      WaitCommand[duration=10 s]"
    })
    @ParameterizedTest(name = "CW line ''{0}'' should be parsed to {1}")
    void assertThatParsingCommandsSucceeds(final String line, final String value) throws Exception {
        final DataItem dataItem = parser.parse(line).get(0);
        assertEquals(value, dataItem.toString());
    }

    @Test
    void assertThatParsingAnInvalidCommandFails() throws Exception {
        assertThatThrownBy(() -> parser.parse(" @Z1000"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Unknown command identifier 'Z'");
    }
}
