package de.do9fse.winkey.lib.core.model.configuration;

import java.text.MessageFormat;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import de.do9fse.winkey.lib.core.model.Validations;

public record WPMSpeedWithReset(Quantity<Dimensionless> value) {
    private static final double WPM_SPEED_RESET = 0;

    public WPMSpeedWithReset {
        Validations.validateNotNull("WPM speed", value);

        final ComparableQuantity<Dimensionless> speed = CustomUnits.asWpm(value);
        final double speedValue = speed.getValue().doubleValue();

        if (speedValue != WPM_SPEED_RESET && (speed.isLessThan(WPMSpeed.WPM_MIN) || speed.isGreaterThan(WPMSpeed.WPM_MAX))) {
            final String message = MessageFormat.format(
                "CW speed must be between {0} and {1} or {2} but was {3}",
                WPMSpeed.WPM_MIN,
                WPMSpeed.WPM_MAX,
                WPM_SPEED_RESET,
                speed
            );
            
            throw new IllegalArgumentException(message);
        }

        value = speed;
    }

    public WPMSpeedWithReset(final WPMSpeed speed) {
        this(Validations.validateNotNull("WPM speed", speed).value());
    }

    public static WPMSpeedWithReset useSpeedPot() {
        return new WPMSpeedWithReset(Quantities.getQuantity(WPM_SPEED_RESET, CustomUnits.WPM));
    }

    public int toProtocolValue() {
        return (int) Math.round(value.getValue().doubleValue());
    }
}
