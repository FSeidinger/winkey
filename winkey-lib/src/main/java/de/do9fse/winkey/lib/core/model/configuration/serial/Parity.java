package de.do9fse.winkey.lib.core.model.configuration.serial;

public enum Parity {
    NO_PARITY,
    EVEN,
    ODD,
    MARK_PARITY,
    SPACE_PARITY;

    public static final Parity DEFAULT = Parity.NO_PARITY;
}
