package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import java.text.MessageFormat;

import javax.measure.quantity.Dimensionless;

import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.CommandConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeed;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeedRange;
import tech.units.indriya.ComparableQuantity;

@CommandConfiguration(allowedProtocols = { WinKeyProtocolVersion.V1, WinKeyProtocolVersion.V2 })
public record SetupSpeedPotCommand(WPMSpeed minWpm, WPMSpeedRange wpmRange, int potRange) implements HostModeCommand {
    public SetupSpeedPotCommand {
        Validations.validateNotNull("Minimum WPM", minWpm);
        Validations.validateNotNull("WPM range", wpmRange);
        Validations.validateRange(
            "Speed pot range",
            potRange,
            Validations.PROTOCOL_BYTE_MIN,
            Validations.PROTOCOL_BYTE_MAX
        );

        checkRangeConstraint(minWpm, wpmRange);
    }

    @Override
    public byte[] getPayloadBytes() {
        return new byte[] {
            0x05,
            (byte) minWpm.toProtocolValue(),
            (byte) wpmRange.toProtocolValue(),
            (byte) potRange
        };
    }

    private void checkRangeConstraint(final WPMSpeed minWpm, final WPMSpeedRange wpmRange) {
        final ComparableQuantity<Dimensionless> maxSpeed = (ComparableQuantity<Dimensionless>) minWpm.value().add(wpmRange.value());

        if (maxSpeed.isGreaterThan(WPMSpeed.WPM_MAX)) {
            final String message = MessageFormat.format(
                "Maximum speed pot WPM must not exceed {0} but was {1}",
                WPMSpeed.WPM_MAX,
                maxSpeed
            );

            throw new IllegalArgumentException(message);
        }
    }
}
