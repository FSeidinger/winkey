package de.do9fse.winkey.lib.core.model.configuration;

import java.text.MessageFormat;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import de.do9fse.winkey.lib.core.model.Validations;

public record WPMSpeed(Quantity<Dimensionless> value) {
    public static final ComparableQuantity<Dimensionless> WPM_MIN = Quantities.getQuantity(5.0, CustomUnits.WPM);
    public static final ComparableQuantity<Dimensionless> WPM_MAX = Quantities.getQuantity(99.0, CustomUnits.WPM);

    public WPMSpeed {
        Validations.validateNotNull("CW Speed value", value);

        final ComparableQuantity<Dimensionless> inWpm = CustomUnits.asWpm(value);

        if (inWpm.isLessThan(WPM_MIN) || inWpm.isGreaterThan(WPM_MAX)) {
            final String message = MessageFormat.format(
                "CW speed must be between {0} and {1} but was {2}",
                WPM_MIN,
                WPM_MAX,
                inWpm
            );
            
            throw new IllegalArgumentException(message);
        }

        value = inWpm;
    }

    public static WPMSpeed fromProtocol(final int value) {
        return new WPMSpeed(Quantities.getQuantity(value & 0xff, CustomUnits.WPM));
    }

    public int toProtocolValue() {
        return (int) Math.round(value.getValue().doubleValue());
    }
}
