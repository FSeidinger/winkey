package de.do9fse.winkey.lib.core.model.configuration.serial;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BaudRateTest {
    @Test
    void assertDefaultBaudRates() {
        assertEquals(1200, BaudRate.DEFAULT.value());
        assertEquals(9600, BaudRate.DEFAULT_ALTERNATIVE.value());
    }

    @Test 
    void assertCustomBaudRate() {
        final int expectedBaudRate = 115200;
        assertEquals(expectedBaudRate, new BaudRate(expectedBaudRate).value());

        assertThatThrownBy(() -> new BaudRate(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Baud rate must be greater than zero");
            
        assertThatThrownBy(() -> new BaudRate(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Baud rate must be greater than zero");
    }

    @Test
    void assertParseCustomBaudRate() {
        final String baudRateValue = "1200";
        final BaudRate baudRate = BaudRate.parseCustomBaudRate(baudRateValue);
        assertEquals(1200, baudRate.value());

        final String nullString = null;
        assertThatThrownBy(() -> BaudRate.parseCustomBaudRate(nullString))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Baud rate value must not be null");

        final String noInteger = "Heinz";
        assertThatThrownBy(() -> BaudRate.parseCustomBaudRate(noInteger))
            .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void assertParseDefaultBaudRate() {
        assertEquals(1200, BaudRate.parseDefaultBaudRate(BaudRate.DEFAULT_KEY).value());
        assertEquals(9600, BaudRate.parseDefaultBaudRate(BaudRate.DEFAULT_ALTERNATIVE_KEY).value());

        assertThatThrownBy(() -> BaudRate.parseDefaultBaudRate("Heinz"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Baud rate must be 'default' or 'alternative'");
    }
}
