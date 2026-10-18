package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import javax.measure.quantity.Dimensionless;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeed;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

@ImmediateCommandConfiguration(
    allowedProtocols = ProtocolVersion.V2
)
public record SetSpeedCommand(WPMSpeed wpmSpeed) implements ImmediateCommand {
    public static final ComparableQuantity<Dimensionless> WPM_MIN = Quantities.getQuantity(5.0, CustomUnits.WPM);
    public static final ComparableQuantity<Dimensionless> WPM_MAX = Quantities.getQuantity(99.0, CustomUnits.WPM);

    public SetSpeedCommand {
        wpmSpeed = new WPMSpeed(
            Validations.validateIntegralRange(
                "WPM Speed",
                CustomUnits.WPM,
                Quantities.getQuantity(wpmSpeed.value().getValue(), wpmSpeed.value().getUnit()),
                WPM_MIN,
                WPM_MAX,
                true
            )
        );
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x02, (byte) wpmSpeed.toProtocolValue() };
    }
}
