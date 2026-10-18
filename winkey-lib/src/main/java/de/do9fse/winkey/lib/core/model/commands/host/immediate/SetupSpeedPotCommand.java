package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import javax.measure.quantity.Dimensionless;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeed;
import tech.units.indriya.ComparableQuantity;

@ImmediateCommandConfiguration(
    allowedProtocols = { ProtocolVersion.V1, ProtocolVersion.V2 }
)
public record SetupSpeedPotCommand(WPMSpeed min, WPMSpeed range) implements ImmediateCommand {
    public SetupSpeedPotCommand {
        Validations.validateRange(
            "WPM min",
            CustomUnits.WPM,
            CustomUnits.asWpm(min.value()),
            Validations.WPM_MIN,
            Validations.WPM_MAX
        );

        final ComparableQuantity<Dimensionless> sum = CustomUnits.asWpm(min.value()).add(range.value());

        Validations.validateRange(
            "Min plus range",
            CustomUnits.WPM,
            sum,
            Validations.WPM_MIN,
            Validations.WPM_MAX
        );
    }

    @Override
    public byte[] toData() {
        return new byte[] {
            0x05,
            (byte) min.toProtocolValue(),
            (byte) range.toProtocolValue(),
            0
        };
    }
}
