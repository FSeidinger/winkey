package de.do9fse.winkey.lib.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.measure.quantity.Dimensionless;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

class ValidationsTest {
    @ParameterizedTest()
    @CsvSource(
        useHeadersInDisplayName = true,
        value = {
        "Value, Min, Max, Zero allowed, Expected",
        "    1,   1,  50,        false,        1",
        "   50,   1,  50,        false,       50",
        "   25,   1,  50,        false,       25",
        "    0,   1,  50,         true,        0",
    })
    void validateIntegralRangeSucceeds(
        final double doubleValue,
        final double min,
        final double max,
        boolean isZeroAllowed,
        final double expectedValue
    ) {
        final ComparableQuantity<Dimensionless> value = Quantities.getQuantity(doubleValue, AbstractUnit.ONE);
        final ComparableQuantity<Dimensionless> expected = Quantities.getQuantity(expectedValue, CustomUnits.WPM);

        assertThat(
            Validations.validateIntegralRange(
                "WPM",
                CustomUnits.WPM,
                value,
                Quantities.getQuantity(min, CustomUnits.WPM),
                Quantities.getQuantity(max, CustomUnits.WPM),
                isZeroAllowed
            )
        ).isEqualTo(expected);
    }
    
    @ParameterizedTest()
    @CsvSource(
        useHeadersInDisplayName = true,
        value = {
        "Value, Min, Max, Zero allowed, Expected Error",
        "    0,   2,  50,        false, WPM must be an integer between 2 WPM and 50 WPM but was 0.0 WPM",
        "    1,   2,  50,        false, WPM must be an integer between 2 WPM and 50 WPM but was 1 WPM",
        "   51,   2,  50,        false, WPM must be an integer between 2 WPM and 50 WPM but was 51 WPM"
    })
    void validateIntegralRangeFails(
        final double doubleValue,
        final double min,
        final double max,
        boolean isZeroAllowed,
        final String expectedMessage
    ) {
        final ComparableQuantity<Dimensionless> value = Quantities.getQuantity(doubleValue, AbstractUnit.ONE);

        assertThatThrownBy(() ->
            Validations.validateIntegralRange(
                "WPM",
                CustomUnits.WPM,
                value,
                Quantities.getQuantity(min, CustomUnits.WPM),
                Quantities.getQuantity(max, CustomUnits.WPM),
                isZeroAllowed
            )
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(expectedMessage);
    }
}
