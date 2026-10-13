package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import javax.measure.Quantity;
import javax.measure.quantity.Time;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record TimedKeyDownCommand(Quantity<Time> duration) implements HostModeCommand {
    public TimedKeyDownCommand {
        Validations.validateNotNull("Timed key down duration", duration);
        
        duration = Quantities.getQuantity(
            Validations.validateIntegralDurationSeconds(
                "Timed key down",
                duration,
                Validations.DURATION_SECONDS_MIN,
                Validations.DURATION_SECONDS_MAX
            ),
            Units.SECOND
        );
    }

    @Override
    public byte[] toData() {
        return new byte[] {
            0x19,
            (byte) Validations.validateIntegralDurationSeconds(
                "Timed key down",
                duration,
                Validations.DURATION_SECONDS_MIN,
                Validations.DURATION_SECONDS_MAX
            )
        };
    }
}
