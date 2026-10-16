package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import static de.do9fse.winkey.lib.testtools.CommandTestTools.assertPayload;
import static javax.measure.MetricPrefix.MILLI;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.configuration.DitDahRatio;
import de.do9fse.winkey.lib.core.model.configuration.FarnsworthSpeed;
import de.do9fse.winkey.lib.core.model.configuration.FirstExtensionDelay;
import de.do9fse.winkey.lib.core.model.configuration.KeyCompensation;
import de.do9fse.winkey.lib.core.model.configuration.KeyMode;
import de.do9fse.winkey.lib.core.model.configuration.ModeRegister;
import de.do9fse.winkey.lib.core.model.configuration.PaddleSetpoint;
import de.do9fse.winkey.lib.core.model.configuration.PinConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeed;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeedRange;
import de.do9fse.winkey.lib.core.model.configuration.WPMSpeedWithReset;
import de.do9fse.winkey.lib.core.model.configuration.Weighting;
import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import de.do9fse.winkey.lib.core.model.responses.SpeedPotValueResponse;
import de.do9fse.winkey.lib.core.model.responses.StatusResponse;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

class ImmediateHostModeCommandTest {
    @Test
    void sideToneControlEncodesEveryFrequencyAndPaddleOnlyFlag() {
        final SideToneControlCommand.SideToneFrequency[] frequencies =
            SideToneControlCommand.SideToneFrequency.values();

        for (int code = 1; code <= frequencies.length; code++) {
            assertTrue(frequencies[code - 1].frequency().getValue().doubleValue() > 0);
            assertPayload(
                new SideToneControlCommand(false, frequencies[code - 1]),
                new byte[] { 0x01, (byte) code }
            );
            assertPayload(
                new SideToneControlCommand(true, frequencies[code - 1]),
                new byte[] { 0x01, (byte) (0x80 | code) }
            );
        }
    }

    @Test
    void sideToneFrequencyEncodesEveryFrequency() {
        final SideToneFrequencyCommand.SideToneFrequency[] frequencies =
            SideToneFrequencyCommand.SideToneFrequency.values();

        for (int code = 1; code <= frequencies.length; code++) {
            assertTrue(frequencies[code - 1].frequency().getValue().doubleValue() > 0);
            assertPayload(
                new SideToneFrequencyCommand(frequencies[code - 1]),
                new byte[] { 0x01, (byte) code }
            );
        }
    }

    @Test
    void encodesImmediateHostCommands() {
        assertPayload(SetSpeedCommand.useSpeedPot(), new byte[] { 0x02, 0x00 });
        assertPayload(
            new SetSpeedCommand(new WPMSpeedWithReset(WPMSpeed.fromProtocol(99))),
            new byte[] { 0x02, 0x63 }
        );
        assertPayload(
            new SetWeightingCommand(Weighting.fromProtocol(50)),
            new byte[] { 0x03, 0x32 }
        );
        assertPayload(
            new SetPttLeadTailCommand(
                Quantities.getQuantity(10, MILLI(Units.SECOND)),
                Quantities.getQuantity(2500, MILLI(Units.SECOND))
            ),
            new byte[] { 0x04, 0x01, (byte) 0xfa }
        );
        assertPayload(
            new SetupSpeedPotCommand(WPMSpeed.fromProtocol(10), WPMSpeedRange.fromProtocol(15), 42),
            new byte[] { 0x05, 0x0a, 0x0f, 0x2a }
        );
        assertPayload(new PauseCommand(true), new byte[] { 0x06, 0x01 });
        assertPayload(new GetSpeedPotCommand(), new byte[] { 0x07 });
        assertPayload(new BackspaceCommand(), new byte[] { 0x08 });
        assertPayload(
            new SetPinConfigurationCommand(new PinConfiguration(
                true,
                true,
                true,
                true,
                PinConfiguration.UltimaticPriority.DIT,
                PinConfiguration.PaddleHangTime.ONE_AND_TWO_THIRDS_LETTERSPACES
            )),
            new byte[] { 0x09, (byte) 0xaf }
        );
        assertPayload(new ClearBufferCommand(), new byte[] { 0x0a });
        assertPayload(new KeyImmediateCommand(true), new byte[] { 0x0b, 0x01 });
        assertPayload(
            new SetHscwSpeedCommand(Quantities.getQuantity(2000, CustomUnits.LPM)),
            new byte[] { 0x0c, 0x14 }
        );
        assertPayload(
            new SetFarnsworthSpeedCommand(FarnsworthSpeed.fromProtocol(18)),
            new byte[] { 0x0d, 0x12 }
        );
        assertPayload(
            new SetModeCommand(new ModeRegister(true, true, KeyMode.BUG_MODE, true, true, true, true)),
            new byte[] { 0x0e, 0x7f }
        );

        final byte[] defaults = new byte[15];
        defaults[0] = 0x31;
        defaults[14] = (byte) 0xff;
        final byte[] loadPayload = new byte[16];
        loadPayload[0] = 0x0f;
        System.arraycopy(defaults, 0, loadPayload, 1, defaults.length);
        assertPayload(new LoadDefaultsCommand(defaults), loadPayload);

        assertPayload(
            new SetFirstExtensionCommand(FirstExtensionDelay.fromProtocol(80)),
            new byte[] { 0x10, 0x50 }
        );
        assertPayload(
            new SetKeyCompensationCommand(KeyCompensation.fromProtocol(180)),
            new byte[] { 0x11, (byte) 0xb4 }
        );
        assertPayload(
            new SetPaddleSwitchPointCommand(PaddleSetpoint.fromProtocol(55)),
            new byte[] { 0x12, 0x37 }
        );
        assertPayload(new NullCommand(), new byte[] { 0x13 });
        assertPayload(
            new SoftwarePaddleCommand(SoftwarePaddleCommand.PaddleSelection.BOTH),
            new byte[] { 0x14, 0x03 }
        );
        assertPayload(new RequestStatusCommand(), new byte[] { 0x15 });
        assertPayload(SetBufferPointerCommand.reset(), new byte[] { 0x16, 0x00 });
        assertPayload(
            new SetBufferPointerCommand(SetBufferPointerCommand.Operation.OVERWRITE, 8),
            new byte[] { 0x16, 0x01, 0x08 }
        );
        assertPayload(
            new SetBufferPointerCommand(SetBufferPointerCommand.Operation.INSERT, 9),
            new byte[] { 0x16, 0x02, 0x09 }
        );
        assertPayload(
            new SetBufferPointerCommand(SetBufferPointerCommand.Operation.ADD_NULLS, 10),
            new byte[] { 0x16, 0x03, 0x0a }
        );
        assertPayload(
            new SetDitDahRatioCommand(DitDahRatio.fromProtocol(50)),
            new byte[] { 0x17, 0x32 }
        );
        assertPayload(new MergeLettersCommand('R', 'R'), new byte[] { 0x1b, 'R', 'R' });
        assertPayload(
            new TimedKeyDownCommand(Quantities.getQuantity(99, Units.SECOND)),
            new byte[] { 0x19, 0x63 }
        );
    }

    @Test
    void hostCommandsDeclareProtocolAndResponseMetadata() {
        final ImmediateCommand setupSpeedPotCommand = new SetupSpeedPotCommand(
            WPMSpeed.fromProtocol(10),
            WPMSpeedRange.fromProtocol(15),
            42
        );
        assertTrue(setupSpeedPotCommand.getCommandInfo().supportsProtocol(ProtocolVersion.V1));
        assertTrue(setupSpeedPotCommand.getCommandInfo().supportsProtocol(ProtocolVersion.V2));

        final ImmediateCommand speedPotCommand = new GetSpeedPotCommand();
        assertTrue(speedPotCommand.getCommandInfo().supportsProtocol(ProtocolVersion.V2));
        assertEquals(SpeedPotValueResponse.class, speedPotCommand.getCommandInfo().responseType());

        final ImmediateCommand statusCommand = new RequestStatusCommand();
        assertTrue(statusCommand.getCommandInfo().supportsProtocol(ProtocolVersion.V2));
        assertEquals(StatusResponse.class, statusCommand.getCommandInfo().responseType());
        assertFalse(statusCommand.getCommandInfo().supportsProtocol(ProtocolVersion.V1));
    }

    @Test
    void validatesProtocolRangesAndCopiesLoadDefaults() {
        final IllegalArgumentException belowMinimumSpeed = assertThrows(
            IllegalArgumentException.class,
            () -> new WPMSpeedWithReset(Quantities.getQuantity(4, CustomUnits.WPM))
        );
        assertEquals(
            "CW speed must be between "
                + WPMSpeed.WPM_MIN
                + " and "
                + WPMSpeed.WPM_MAX
                + " or 0 but was 4 WPM",
            belowMinimumSpeed.getMessage()
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new WPMSpeedWithReset(Quantities.getQuantity(100, CustomUnits.WPM))
        );
        assertEquals(
            46,
            new WPMSpeedWithReset(Quantities.getQuantity(45.5, CustomUnits.WPM)).toProtocolValue()
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new SetPttLeadTailCommand(
                Quantities.getQuantity(2510, MILLI(Units.SECOND)),
                Quantities.getQuantity(0, Units.SECOND)
            )
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new SetupSpeedPotCommand(
                WPMSpeed.fromProtocol(90),
                WPMSpeedRange.fromProtocol(10),
                0
            )
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new SetHscwSpeedCommand(Quantities.getQuantity(900, CustomUnits.LPM))
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new TimedKeyDownCommand(Quantities.getQuantity(100, Units.SECOND))
        );
        assertThrows(IllegalArgumentException.class, () -> new MergeLettersCommand('A', '\u0080'));
        assertThrows(IllegalArgumentException.class, () -> new LoadDefaultsCommand(new byte[14]));

        final byte[] values = new byte[15];
        final LoadDefaultsCommand command = new LoadDefaultsCommand(values);
        values[0] = 0x01;
        assertArrayEquals(new byte[15], command.values());

        final byte[] returnedValues = command.values();
        returnedValues[0] = 0x02;
        assertArrayEquals(new byte[15], command.values());
    }

}
