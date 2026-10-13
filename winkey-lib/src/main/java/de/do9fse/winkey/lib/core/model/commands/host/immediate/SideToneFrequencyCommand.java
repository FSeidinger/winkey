package de.do9fse.winkey.lib.core.model.commands.host.immediate;


import javax.measure.Quantity;
import javax.measure.quantity.Frequency;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

@ImmediateCommandConfiguration(
    allowedProtocols = { ProtocolVersion.V1, ProtocolVersion.V2 }
)
public record SideToneFrequencyCommand(SideToneFrequency sideToneFrequency) implements ImmediateCommand {
    public enum SideToneFrequency {
        FREQUENCY_3759_HZ(3759),
        FREQUENCY_1879_HZ(1879),
        FREQUENCY_1252_HZ(1252),
        FREQUENCY_940_HZ(940),
        FREQUENCY_752_HZ(752),
        FREQUENCY_625_HZ(625),
        FREQUENCY_535_HZ(535),
        FREQUENCY_469_HZ(469),
        FREQUENCY_417_HZ(417),
        FREQUENCY_375_HZ(375);

        private final Quantity<Frequency> frequency;

        SideToneFrequency(final int frequencyHz) {
            this.frequency = Quantities.getQuantity(frequencyHz, Units.HERTZ);
        }

        public Quantity<Frequency> frequency() {
            return frequency;
        }
    }

    public SideToneFrequencyCommand {
        Validations.validateNotNull("sideToneFrequency", sideToneFrequency);
    }

    @Override
    public byte[] toData() {
        byte sideToneFrequencyByte = switch(sideToneFrequency) {
            case FREQUENCY_3759_HZ -> (byte) 0x01;
            case FREQUENCY_1879_HZ -> (byte) 0x02;
            case FREQUENCY_1252_HZ -> (byte) 0x03;
            case FREQUENCY_940_HZ -> (byte) 0x04;
            case FREQUENCY_752_HZ -> (byte) 0x05;
            case FREQUENCY_625_HZ -> (byte) 0x06;
            case FREQUENCY_535_HZ -> (byte) 0x07;
            case FREQUENCY_469_HZ -> (byte) 0x08;
            case FREQUENCY_417_HZ -> (byte) 0x09;
            case FREQUENCY_375_HZ -> (byte) 0x0a;
        };

        return new byte[]{ 0x01, sideToneFrequencyByte };
    }
}
