package de.do9fse.winkey.lib.core.model.commands.host.buffered;


import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.BufferedCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record ChangeSpeedBufferedCommand(Quantity<Dimensionless> wpmSpeed) implements BufferedCommand {
    public static final ComparableQuantity<Dimensionless> WPM_MIN = Quantities.getQuantity(5.0, CustomUnits.WPM);
    public static final ComparableQuantity<Dimensionless> WPM_MAX = Quantities.getQuantity(99.0, CustomUnits.WPM);

    public ChangeSpeedBufferedCommand {
        Validations.validateNotNull("WPM speed", wpmSpeed);
        
        wpmSpeed = Validations.validateIntegerRange(
            "WPM speed",
            CustomUnits.WPM,
            CustomUnits.asWpm(wpmSpeed),
            WPM_MIN,
            WPM_MAX
        );
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x1c, (byte) wpmSpeed.getValue().intValue() };
    }
}
