package de.do9fse.winkey.lib.core.model.configuration;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import de.do9fse.winkey.lib.core.model.Validations;
import tech.units.indriya.quantity.Quantities;

public record WPMSpeed(Quantity<Dimensionless> value) {
    public WPMSpeed {
        value = Quantities
            .getQuantity(
                Validations.validateNotNull("WPM speed", value).getValue(),
                value.getUnit()
            )
            .to(CustomUnits.WPM);
    }

    public static WPMSpeed fromProtocol(final int value) {
        return new WPMSpeed(Quantities.getQuantity(value, CustomUnits.WPM));
    }

    public int toProtocolValue() {
        return value.getValue().intValue();
    }
}
