package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import static javax.measure.MetricPrefix.MILLI;

import javax.measure.Quantity;
import javax.measure.quantity.Time;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record SetPttLeadTailCommand(Quantity<Time> leadIn, Quantity<Time> tail) implements HostModeCommand {
    public SetPttLeadTailCommand {
        Validations.validateNotNull("PTT lead-in", leadIn);

        Validations.validateNotNull("PTT tail", tail);

        final int leadInUnits = Validations.validatePttDelay10Ms(
            "PTT lead-in",
            leadIn,
            Validations.PTT_DELAY_MILLISECONDS_MIN,
            Validations.PTT_DELAY_MILLISECONDS_MAX,
            Validations.PTT_DELAY_STEP_MILLISECONDS
        );

        leadIn = Quantities.getQuantity(
            leadInUnits * Validations.PTT_DELAY_STEP_MILLISECONDS,
            MILLI(Units.SECOND)
        );

        final int tailUnits = Validations.validatePttDelay10Ms(
            "PTT tail",
            tail,
            Validations.PTT_DELAY_MILLISECONDS_MIN,
            Validations.PTT_DELAY_MILLISECONDS_MAX,
            Validations.PTT_DELAY_STEP_MILLISECONDS
        );
        tail = Quantities.getQuantity(
            tailUnits * Validations.PTT_DELAY_STEP_MILLISECONDS,
            MILLI(Units.SECOND)
        );
    }

    @Override
    public byte[] toData() {
        return new byte[] {
            0x04,
            (byte) Validations.validatePttDelay10Ms(
                "PTT lead-in",
                leadIn,
                Validations.PTT_DELAY_MILLISECONDS_MIN,
                Validations.PTT_DELAY_MILLISECONDS_MAX,
                Validations.PTT_DELAY_STEP_MILLISECONDS
            ),
            (byte) Validations.validatePttDelay10Ms(
                "PTT tail",
                tail,
                Validations.PTT_DELAY_MILLISECONDS_MIN,
                Validations.PTT_DELAY_MILLISECONDS_MAX,
                Validations.PTT_DELAY_STEP_MILLISECONDS
            )
        };
    }
}
