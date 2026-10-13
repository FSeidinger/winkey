package de.do9fse.winkey.lib.core.model.configuration;

import static javax.measure.MetricPrefix.MILLI;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import tech.units.indriya.AbstractUnit;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

class WinKeyUnitsTest {
    @Test
    void convertsDimensionlessQuantitiesToWinKeyUnits() {
        assertEquals(Quantities.getQuantity(20, CustomUnits.WPM), CustomUnits.asWpm(
            Quantities.getQuantity(20, AbstractUnit.ONE)
        ));
        assertEquals(Quantities.getQuantity(3, CustomUnits.RATIO), CustomUnits.asRatio(
            Quantities.getQuantity(3, AbstractUnit.ONE)
        ));
        assertEquals(Quantities.getQuantity(50, Units.PERCENT), CustomUnits.asPercent(
            Quantities.getQuantity(0.5, AbstractUnit.ONE)
        ));
    }

    @Test
    void convertsTimeQuantitiesToMilliseconds() {
        assertEquals(
            Quantities.getQuantity(1500, MILLI(Units.SECOND)),
            CustomUnits.asMilliseconds(Quantities.getQuantity(1.5, Units.SECOND))
        );
    }
}
