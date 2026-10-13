package de.do9fse.winkey.lib.core.model.commands.host.buffered;


import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.BufferedCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeed;
import tech.units.indriya.quantity.Quantities;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record ChangeSpeedBufferedCommand(Quantity<Dimensionless> wpm) implements BufferedCommand {
    public ChangeSpeedBufferedCommand {
        Validations.validateNotNull("Buffered WPM speed", wpm);
        
        wpm = Quantities.getQuantity(Validations.validateIntegralRange(
            "Buffered WPM speed",
            wpm,
            CustomUnits.WPM,
            WPMSpeed.WPM_MIN.getValue().intValue(),
            WPMSpeed.WPM_MAX.getValue().intValue()
        ), CustomUnits.WPM);
    }

    @Override
    public byte[] toData() {
        return new byte[] { 0x1c, (byte) wpm.getValue().intValue() };
    }
}
