package de.do9fse.winkey.lib.core.model.commands.host.buffered;


import javax.measure.Quantity;
import javax.measure.quantity.Time;

import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.CommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.host.immediate.HostModeCommand;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

@CommandConfiguration(allowedProtocols = WinKeyProtocolVersion.V2, isBuffered = true)
public record WaitCommand(Quantity<Time> duration) implements HostModeCommand {
    public WaitCommand {
        Validations.validateNotNull("Buffered wait duration", duration);
        
        duration = Quantities.getQuantity(
            Validations.validateIntegralDurationSeconds(
                "Buffered wait",
                duration,
                Validations.DURATION_SECONDS_MIN,
                Validations.DURATION_SECONDS_MAX
            ),
            Units.SECOND
        );
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] {
            0x1a,
            (byte) Validations.validateIntegralDurationSeconds(
                "Buffered wait",
                duration,
                Validations.DURATION_SECONDS_MIN,
                Validations.DURATION_SECONDS_MAX
            )
        };
    }
}
