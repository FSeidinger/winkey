package de.do9fse.winkey.lib.core.model;

import static java.lang.Math.abs;
import static java.lang.Math.rint;

import java.text.MessageFormat;
import java.util.Objects;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Time;

import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

public class Validations {
    public static final int PROTOCOL_BYTE_MIN = 0;
    public static final int PROTOCOL_BYTE_MAX = 255;

    public static final int DURATION_SECONDS_MIN = 0;
    public static final int DURATION_SECONDS_MAX = 99;

    public static final int PTT_DELAY_MILLISECONDS_MIN = 0;
    public static final int PTT_DELAY_MILLISECONDS_MAX = 2500;
    public static final int PTT_DELAY_STEP_MILLISECONDS = 10;

    public static final int HSCW_SPEED_MIN_LPM = 1000;
    public static final int HSCW_SPEED_MAX_LPM = 8000;
    public static final int HSCW_SPEED_STEP_LPM = 100;

    public static final ComparableQuantity<Dimensionless> WPM_MIN = Quantities.getQuantity(5.0, CustomUnits.WPM);
    public static final ComparableQuantity<Dimensionless> WPM_MAX = Quantities.getQuantity(99.0, CustomUnits.WPM);

    public static <T> T validateNotNull(final String fieldName, final T value) {
        return Objects.requireNonNull(value, fieldName + " must not be null");
    }

    public static String validateNotBlank(final String fieldName, final String value) {
        validateNotNull(fieldName, value);

        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        return value;
    }

    public static int validateGreaterThanZero(final String fieldName, final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero");
        }

        return value;
    }

    public static ComparableQuantity<Dimensionless> validateRange(
        final String fieldName,
        final Unit<Dimensionless> unit,
        final ComparableQuantity<Dimensionless> value,
        final ComparableQuantity<Dimensionless> min,
        final ComparableQuantity<Dimensionless> max   
    ) {
        validateNotBlank("Fieldname", fieldName);
        validateNotNull(fieldName + " unit", unit);
        validateNotNull(fieldName + " min value", min);
        validateNotNull(fieldName + " max value", max);

        final ComparableQuantity<Dimensionless> inUnits = validateNotNull(fieldName, value).to(unit);

        if (value.isGreaterThanOrEqualTo(min) && value.isLessThanOrEqualTo(max)) {
            return inUnits;
        }

        throw new IllegalArgumentException(
            fieldName + "must be between " + min + " and " + max + " but was " + inUnits
        );
    }

    public static ComparableQuantity<Dimensionless> validateIntegerRange(
        final String fieldName,
        final Unit<Dimensionless> unit,
        final ComparableQuantity<Dimensionless> value,
        final ComparableQuantity<Dimensionless> min,
        final ComparableQuantity<Dimensionless> max
    ) {
        return validateIntegralRange(fieldName, unit, value, min, max, false);
    }

    public static ComparableQuantity<Dimensionless> validateIntegralRange(
        final String fieldName,
        final Unit<Dimensionless> unit,
        final ComparableQuantity<Dimensionless> value,
        final ComparableQuantity<Dimensionless> min,
        final ComparableQuantity<Dimensionless> max,
        final boolean isZeroAllowed
    ) {
        validateNotBlank("Fieldname", fieldName);
        validateNotNull(fieldName + " unit", unit);
        validateNotNull(fieldName + " min value", min);
        validateNotNull(fieldName + " max value", max);

        final ComparableQuantity<Dimensionless> inWPM = validateNotNull(fieldName, value).to(unit);

        final double epsilon = 1e-6;
        final double doubleValue = inWPM.getValue().doubleValue();
        final boolean isInt = abs(rint(doubleValue) - doubleValue) < epsilon;
        final boolean isZero = abs(0.0 - doubleValue) < epsilon;

        if (isZeroAllowed && isZero) {
            return inWPM;
        }

        if (value.isGreaterThanOrEqualTo(min) && value.isLessThanOrEqualTo(max) && isInt) {
            return inWPM;
        }

        final String messagePrefix;
        if (isZeroAllowed) {
            messagePrefix = fieldName + " must either be zero or an integer between ";
        } else {
            messagePrefix = fieldName + " must be an integer between ";
        }

        final String message = messagePrefix + min + " and " + max + " but was " + inWPM;
        throw new IllegalArgumentException(
            message
        );
    }

    public static int validateIntegralDurationSeconds(
        final String fieldName,
        final Quantity<Time> duration,
        final int minSeconds,
        final int maxSeconds
    ) {
        final double seconds = duration.to(Units.SECOND).getValue().doubleValue();
        if (seconds < minSeconds || seconds > maxSeconds || seconds != rint(seconds)) {
            final String message = MessageFormat.format(
                "{0} must be an integer between {1} and {2} seconds but was {3}",
                fieldName,
                minSeconds,
                maxSeconds,
                seconds
            );

            throw new IllegalArgumentException(message);
        }
        return (int) seconds;
    }

    public static int validateIntegralFrequencyRange(
        final String fieldName,
        final Quantity<Frequency> quantity,
        final Unit<Frequency> unit,
        final int min,
        final int max
    ) {
        final double value = quantity.to(unit).getValue().doubleValue();
        if (value < min || value > max || value != rint(value)) {
            final String message = MessageFormat.format(
                "{0} must be an integer between {1} and {2} but was {3}",
                fieldName,
                min,
                max,
                value
            );

            throw new IllegalArgumentException(message);
        }
        return (int) value;
    }

    public static int validatePttDelay10Ms(
        final String fieldName,
        final Quantity<Time> duration,
        final int minMilliseconds,
        final int maxMilliseconds,
        final int stepMilliseconds
    ) {
        final double milliseconds = CustomUnits.asMilliseconds(duration).getValue().doubleValue();
        if (milliseconds < minMilliseconds
            || milliseconds > maxMilliseconds
            || milliseconds % stepMilliseconds != 0) {
            final String message = MessageFormat.format(
                "{0} must be a multiple of {1} ms between {2} and {3} ms but was {4} ms",
                fieldName,
                stepMilliseconds,
                minMilliseconds,
                maxMilliseconds,
                milliseconds
            );

            throw new IllegalArgumentException(message);
        }

        return (int) (milliseconds / stepMilliseconds);
    }
}
