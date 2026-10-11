package de.do9fse.winkey.lib.core.model.configuration.serial;

public enum StopBits {
    ONE,
    ONE_POINT_FIVE_STOP_BITS,
    TWO_STOP_BITS;

    public static final StopBits DEFAULT = StopBits.ONE;
}
