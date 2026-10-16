package de.do9fse.winkey.lib.core.model;

public interface Status {
    static final int MASK_TAG           = 0b11100000;
    static final int MASK_WAIT          = 0b00010000;
    static final int MASK_KEYDOWN       = 0b00001000;
    static final int MASK_BUSY          = 0b00000100;
    static final int MASK_BREAKIN       = 0b00000010;
    static final int MASK_XOFF          = 0b00000001;

    static final int MASK_PUSH_BUTTON   = 0b00001000;

    static final int MASK_PB4           = 0b00010000;
    static final int MASK_PB3           = 0b00000100;
    static final int MASK_PB2           = 0b00000010;
    static final int MASK_PB1           = 0b00000001;

    static Status fromData(final int status, final ProtocolVersion protocolVersion) {
        switch (protocolVersion) {
            case V1: return StatusV1.fromData(status);
            case V2: return decodeV2(status);
            
            default: throw new IllegalArgumentException("Unsupported protocol version " + protocolVersion);
        }
    }

    static boolean filter(final int value, final int mask) {
        return (value & mask) == mask;
    }

    static Status decodeV2(final int status) {
        final boolean isPushButton = filter(status, MASK_PUSH_BUTTON);

        if (isPushButton) {
            return StatusPushButton.fromData(status);
        } else {
            return StatusV2.fromData(status);
        }
    }
}
