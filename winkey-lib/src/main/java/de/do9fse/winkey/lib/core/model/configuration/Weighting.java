package de.do9fse.winkey.lib.core.model.configuration;

import java.text.MessageFormat;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;
import de.do9fse.winkey.lib.core.model.Validations;

public record Weighting(Quantity<Dimensionless> value) {
    public static final ComparableQuantity<Dimensionless> PERCENT_MIN = Quantities.getQuantity(10, Units.PERCENT);
    public static final ComparableQuantity<Dimensionless> PERCENT_MAX = Quantities.getQuantity(90, Units.PERCENT);

    public Weighting {
        Validations.validateNotNull("Weighting value", value);

        final ComparableQuantity<Dimensionless> inPercent = CustomUnits.asPercent(value);

        if (inPercent.isLessThan(PERCENT_MIN) || inPercent.isGreaterThan(PERCENT_MAX)) {
            final String message = MessageFormat.format(
                "Weighting must be between {0} and {1} but was {2}",
                PERCENT_MIN,
                PERCENT_MAX,
                inPercent
            );
            throw new IllegalArgumentException(message);
        }

        value = inPercent;
    }

    public static Weighting fromProtocol(final int value) {
        return new Weighting(Quantities.getQuantity(value & 0xff, Units.PERCENT));
    }

    public int toProtocolValue() {
        return (int) Math.round(value.getValue().doubleValue());
    }
}
