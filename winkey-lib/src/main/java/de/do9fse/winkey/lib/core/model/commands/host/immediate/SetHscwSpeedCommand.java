package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import java.text.MessageFormat;

import javax.measure.Quantity;
import javax.measure.quantity.Frequency;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import tech.units.indriya.quantity.Quantities;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record SetHscwSpeedCommand(Quantity<Frequency> lpm) implements HostModeCommand {
    public SetHscwSpeedCommand {
        Validations.validateNotNull("HSCW speed", lpm);

        final int lpmValue = Validations.validateIntegralFrequencyRange(
            "HSCW speed",
            lpm,
            CustomUnits.LPM,
            Validations.HSCW_SPEED_MIN_LPM,
            Validations.HSCW_SPEED_MAX_LPM
        );

        if (lpmValue % Validations.HSCW_SPEED_STEP_LPM != 0) {
            final String message = MessageFormat.format(
                "HSCW speed must be a multiple of {0} LPM but was {1} LPM",
                Validations.HSCW_SPEED_STEP_LPM,
                lpmValue
            );
            throw new IllegalArgumentException(message);
        }
        
        lpm = Quantities.getQuantity(lpmValue, CustomUnits.LPM);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] {
            0x0c,
            (byte) (lpm.getValue().intValue() / Validations.HSCW_SPEED_STEP_LPM)
        };
    }
}
