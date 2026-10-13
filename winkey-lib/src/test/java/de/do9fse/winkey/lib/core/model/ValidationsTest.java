package de.do9fse.winkey.lib.core.model;

import static javax.measure.MetricPrefix.MILLI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

class ValidationsTest {
    @Test
    void validatesIntegerRange() {
        assertThat(Validations.validateRange("value", 5, 0, 10)).isEqualTo(5);

        assertThatThrownBy(() -> Validations.validateRange("value", 11, 0, 10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("value must be between 0 and 10 but was 11");
    }

    @Test
    void validatesIntegralDimensionlessQuantityRange() {
        assertThat(Validations.validateIntegralRange(
            "WPM",
            Quantities.getQuantity(25, CustomUnits.WPM),
            CustomUnits.WPM,
            1,
            50
        )).isEqualTo(25);

        assertThatThrownBy(() -> Validations.validateIntegralRange(
            "WPM",
            Quantities.getQuantity(25.5, CustomUnits.WPM),
            CustomUnits.WPM,
            1,
            50
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("WPM must be an integer between 1 and 50 but was 25.5");
    }

    @Test
    void validatesIntegralDurationInSeconds() {
        assertThat(Validations.validateIntegralDurationSeconds(
            "duration",
            Quantities.getQuantity(2, Units.SECOND),
            0,
            99
        )).isEqualTo(2);

        assertThatThrownBy(() -> Validations.validateIntegralDurationSeconds(
            "duration",
            Quantities.getQuantity(1500, MILLI(Units.SECOND)),
            0,
            99
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("duration must be an integer between 0 and 99 seconds but was 1.5");
    }

    @Test
    void validatesIntegralFrequencyRange() {
        assertThat(Validations.validateIntegralFrequencyRange(
            "HSCW speed",
            Quantities.getQuantity(120, CustomUnits.LPM),
            CustomUnits.LPM,
            100,
            800
        )).isEqualTo(120);

        assertThatThrownBy(() -> Validations.validateIntegralFrequencyRange(
            "HSCW speed",
            Quantities.getQuantity(120.5, CustomUnits.LPM),
            CustomUnits.LPM,
            100,
            800
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("HSCW speed must be an integer between 100 and 800 but was 120.5");
    }

    @Test
    void validatesPttDelayRangeAndStep() {
        assertThat(Validations.validatePttDelay10Ms(
            "PTT lead-in",
            Quantities.getQuantity(150, MILLI(Units.SECOND)),
            0,
            2500,
            10
        )).isEqualTo(15);

        assertThatThrownBy(() -> Validations.validatePttDelay10Ms(
            "PTT lead-in",
            Quantities.getQuantity(155, MILLI(Units.SECOND)),
            0,
            2500,
            10
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("must be a multiple of 10 ms between 0 and")
            .hasMessageContaining("but was 155 ms");
    }
}
