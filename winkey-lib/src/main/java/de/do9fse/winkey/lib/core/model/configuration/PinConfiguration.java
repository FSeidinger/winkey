package de.do9fse.winkey.lib.core.model.configuration;

import de.do9fse.winkey.lib.core.model.Validations;

public record PinConfiguration(
    boolean isPttEnabled,
    boolean isSidetoneEnabled,
    boolean isKeyOutput2Enabled,
    boolean isKeyOutput1Enabled,
    UltimaticPriority ultimaticPriority,
    PaddleHangTime paddleHangTime
) {
    public enum UltimaticPriority {
        NORMAL,
        DAH,
        DIT,
        UNDEFINED
    }

    public enum PaddleHangTime {
        ONE_LETTERSPACE,
        ONE_AND_ONE_THIRD_LETTERSPACES,
        ONE_AND_TWO_THIRDS_LETTERSPACES,
        TWO_LETTERSPACES
    }

    public PinConfiguration {
        Validations.validateNotNull("Ultimatic priority", ultimaticPriority);
        Validations.validateNotNull("Paddle hang time", paddleHangTime);
    }

    public static PinConfiguration fromProtocol(final int value) {
        final int priorityCode = (value >>> 6) & 0b11;
        final int hangTimeCode = (value >>> 4) & 0b11;

        return new PinConfiguration(
            (value & 0b00000001) != 0,
            (value & 0b00000010) != 0,
            (value & 0b00000100) != 0,
            (value & 0b00001000) != 0,
            UltimaticPriority.values()[priorityCode],
            PaddleHangTime.values()[hangTimeCode]
        );
    }

    public int toProtocolValue() {
        return (isPttEnabled ? 0x01 : 0)
            | (isSidetoneEnabled ? 0x02 : 0)
            | (isKeyOutput2Enabled ? 0x04 : 0)
            | (isKeyOutput1Enabled ? 0x08 : 0)
            | (paddleHangTime.ordinal() << 4)
            | (ultimaticPriority.ordinal() << 6);
    }
}
