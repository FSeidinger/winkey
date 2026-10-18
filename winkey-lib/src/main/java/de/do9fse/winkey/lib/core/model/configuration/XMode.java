package de.do9fse.winkey.lib.core.model.configuration;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

public record XMode(
    boolean standaloneCutMode,
    boolean enablePaddleStatus,
    Quantity<Dimensionless> letterspaceAdjustment
) {
    public static final int MASK_CUT_MODE = 0b10000000;
    public static final int MASK_PADDLE_STATUS = 0b00100000;

    public static final Quantity<Dimensionless> LETTER_SPACE_ADJUSTMENT_MIN = Quantities.getQuantity(0, Units.PERCENT);
    public static final Quantity<Dimensionless> LETTER_SPACE_ADJUSTMENT_MAX = Quantities.getQuantity(30, Units.PERCENT);

    public XMode {
        final ComparableQuantity<Dimensionless> letterspaceAdjustmentInPercent =
            Quantities.getQuantity(
                letterspaceAdjustment.getValue(),
                letterspaceAdjustment.getUnit()
            ).to(Units.PERCENT);

        if (
            letterspaceAdjustmentInPercent.isLessThan(LETTER_SPACE_ADJUSTMENT_MIN)
            ||
            letterspaceAdjustmentInPercent.isGreaterThan(LETTER_SPACE_ADJUSTMENT_MAX)
        ) {
            throw new IllegalArgumentException(
                "Letterspace adjustment must be in the range of " +
                LETTER_SPACE_ADJUSTMENT_MIN +
                " to " +
                LETTER_SPACE_ADJUSTMENT_MAX +
                " but was " +
                letterspaceAdjustmentInPercent
            );          
        }

        if (!isEven(letterspaceAdjustmentInPercent)) {
            throw new IllegalArgumentException(
                "Letterspace adjustment must be an even value in 2 % increments, e.g. " +
                "'" + LETTER_SPACE_ADJUSTMENT_MIN + "' " + 
                "'2 %', ..., " +
                "'" + LETTER_SPACE_ADJUSTMENT_MAX + "' " 
            );
        }

    }

    public int toProtocolValue() {
        int protocolRepresentation = 0;

        protocolRepresentation |= standaloneCutMode ? MASK_CUT_MODE : 0;
        protocolRepresentation |= enablePaddleStatus ? MASK_PADDLE_STATUS : 0;
        protocolRepresentation += letterspaceAdjustment.getValue().intValue() / 2;

        return protocolRepresentation;
    }

    private boolean isEven(final ComparableQuantity<Dimensionless> value) {
        return (value.getValue().intValue() & 0x01) == 0;
    }
}
