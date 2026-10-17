package de.do9fse.winkey.lib.core.model.configuration;

import static javax.measure.MetricPrefix.MILLI;

import java.text.MessageFormat;

import javax.measure.Quantity;
import javax.measure.quantity.Time;

import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;
import de.do9fse.winkey.lib.core.model.Validations;

public record FirstExtensionDelay(Quantity<Time> value) {
    public static final ComparableQuantity<Time> DELAY_MIN = Quantities.getQuantity(0, MILLI(Units.SECOND));
    public static final ComparableQuantity<Time> DELAY_MAX = Quantities.getQuantity(250, MILLI(Units.SECOND));

    public FirstExtensionDelay {
        Validations.validateNotNull("First extension delay", value);

        final ComparableQuantity<Time> inMilliseconds = CustomUnits.asMilliseconds(value);

        if (inMilliseconds.isLessThan(DELAY_MIN) || inMilliseconds.isGreaterThan(DELAY_MAX)) {
            final String message = MessageFormat.format(
                "First extension delay must be between {0} and {1} but was {2}",
                DELAY_MIN,
                DELAY_MAX,
                inMilliseconds
            );

            throw new IllegalArgumentException(message);
        }

        value = inMilliseconds;
    }

    public static FirstExtensionDelay fromProtocol(final int value) {
        return new FirstExtensionDelay(Quantities.getQuantity(value, MILLI(Units.SECOND)));
    }

    public int toProtocolValue() {
        return (int) Math.round(value.getValue().doubleValue());
    }

}
