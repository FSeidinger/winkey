package de.do9fse.winkey.lib.core.model.commands.host.buffered;

import static de.do9fse.winkey.lib.testtools.CommandTestTools.assertPayload;

import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.configuration.WinKeyUnits;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

class BufferedHostModeCommandTest {
    @Test
    void encodesBufferedHostCommands() {
        assertPayload(new PttCommand(true), new byte[] { 0x18, 0x01 });
        assertPayload(
            new WaitCommand(Quantities.getQuantity(15, Units.SECOND)),
            new byte[] { 0x1a, 0x0f }
        );
        assertPayload(
            new ChangeSpeedBufferedCommand(Quantities.getQuantity(25, WinKeyUnits.WPM)),
            new byte[] { 0x1c, 0x19 }
        );
        assertPayload(
            new HscwSpeedChangeCommand(Quantities.getQuantity(4000, WinKeyUnits.LPM)),
            new byte[] { 0x1d, 0x28 }
        );
        assertPayload(new CancelBufferedSpeedCommand(), new byte[] { 0x1e });
        assertPayload(new NopCommand(), new byte[] { 0x1f });
    }
}
