package de.do9fse.winkey.lib.core.model.commands.host.buffered;


import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.CommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.host.immediate.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeed;
import de.do9fse.winkey.lib.core.model.configuration.WinKeyUnits;
import tech.units.indriya.quantity.Quantities;

@CommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2, isBuffered = true)
public record ChangeSpeedBufferedCommand(Quantity<Dimensionless> wpm) implements HostModeCommand {
    public ChangeSpeedBufferedCommand {
        Validations.validateNotNull("Buffered WPM speed", wpm);
        
        wpm = Quantities.getQuantity(Validations.validateIntegralRange(
            "Buffered WPM speed",
            wpm,
            WinKeyUnits.WPM,
            WPMSpeed.WPM_MIN.getValue().intValue(),
            WPMSpeed.WPM_MAX.getValue().intValue()
        ), WinKeyUnits.WPM);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] { 0x1c, (byte) wpm.getValue().intValue() };
    }
}
