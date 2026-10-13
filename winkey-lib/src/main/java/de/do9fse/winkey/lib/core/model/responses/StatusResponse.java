package de.do9fse.winkey.lib.core.model.responses;

import java.text.MessageFormat;

@ResponseConfiguration(expectedResponseBytes = 1)
public abstract class StatusResponse implements Response {
    private static final int RESPONSE_TAG_MASK = 0b11100000;
    private static final int RESPONSE_TAG = 0b11000000;

    private final boolean waiting;
    private final boolean keyDown;
    private final boolean busy;
    private final boolean breakIn;
    private final boolean xoff;
    private final boolean pushButtonStatus;
    private final int pushButtonMask;

    protected StatusResponse(
        final boolean waiting,
        final boolean keyDown,
        final boolean busy,
        final boolean breakIn,
        final boolean xoff,
        final boolean pushButtonStatus,
        final int pushButtonMask
    ) {
        this.waiting = waiting;
        this.keyDown = keyDown;
        this.busy = busy;
        this.breakIn = breakIn;
        this.xoff = xoff;
        this.pushButtonStatus = pushButtonStatus;
        this.pushButtonMask = pushButtonMask;
    }

    protected static int getStatusByte(final byte[] responseBytes) {
        final int value = Byte.toUnsignedInt(responseBytes[0]);

        if ((value & RESPONSE_TAG_MASK) != RESPONSE_TAG) {
            throw new IllegalArgumentException(
                MessageFormat.format("Invalid WinKey status byte: {0}", value)
            );
        }

        return value;
    }

    public boolean waiting() {
        return waiting;
    }

    public boolean keyDown() {
        return keyDown;
    }

    public boolean busy() {
        return busy;
    }

    public boolean breakIn() {
        return breakIn;
    }

    public boolean xoff() {
        return xoff;
    }

    public boolean pushButtonStatus() {
        return pushButtonStatus;
    }

    public int pushButtonMask() {
        return pushButtonMask;
    }
}
