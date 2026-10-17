package de.do9fse.winkey.lib.core.model.configuration;

import de.do9fse.winkey.lib.core.model.Validations;

public record ModeRegister(
    boolean isPaddleWatchdogEnabled,
    boolean isPaddleEchobackEnabled,
    KeyMode keyMode,
    boolean isPaddleSwapped,
    boolean isSerialEchobackEnabled,
    boolean isAutospaceEnabled,
    boolean isCTSpacingEnabled
) {
    public ModeRegister {
        Validations.validateNotNull("Key mode", keyMode);
    }

    public static ModeRegister fromProtocol(final int value) {
        return new ModeRegister(
            (value & 0b10000000) == 0,
            (value & 0b01000000) != 0,
            KeyMode.fromProtocol(value),
            (value & 0b00001000) != 0,
            (value & 0b00000100) != 0,
            (value & 0b00000010) != 0,
            (value & 0b00000001) != 0
        );
    }

    public int toProtocolValue() {
        return (isPaddleWatchdogEnabled ? 0 : 0x80)
            | (isPaddleEchobackEnabled ? 0x40 : 0)
            | keyMode.toProtocolValue()
            | (isPaddleSwapped ? 0x08 : 0)
            | (isSerialEchobackEnabled ? 0x04 : 0)
            | (isAutospaceEnabled ? 0x02 : 0)
            | (isCTSpacingEnabled ? 0x01 : 0);
    }
}
