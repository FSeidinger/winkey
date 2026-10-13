package de.do9fse.winkey.lib.core.model.commands.host.buffered;

import java.text.MessageFormat;

import javax.measure.Quantity;
import javax.measure.quantity.Frequency;

import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.WinKeyUnits;
import tech.units.indriya.quantity.Quantities;

@ImmediateCommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2)
public record HscwSpeedChangeCommand(Quantity<Frequency> lpm) implements HostModeCommand {
    public HscwSpeedChangeCommand {
        Validations.validateNotNull("Buffered HSCW speed", lpm);

        final int lpmValue = Validations.validateIntegralFrequencyRange(
            "Buffered HSCW speed",
            lpm,
            WinKeyUnits.LPM,
            Validations.HSCW_SPEED_MIN_LPM,
            Validations.HSCW_SPEED_MAX_LPM
        );

        if (lpmValue % Validations.HSCW_SPEED_STEP_LPM != 0) {
            final String message = MessageFormat.format(
                "Buffered HSCW speed must be a multiple of {0} LPM but was {1} LPM",
                Validations.HSCW_SPEED_STEP_LPM,
                lpmValue
            );

            throw new IllegalArgumentException(message);
        }
        
        lpm = Quantities.getQuantity(lpmValue, WinKeyUnits.LPM);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] {
            0x1d,
            (byte) (lpm.getValue().intValue() / Validations.HSCW_SPEED_STEP_LPM)
        };
    }
}
