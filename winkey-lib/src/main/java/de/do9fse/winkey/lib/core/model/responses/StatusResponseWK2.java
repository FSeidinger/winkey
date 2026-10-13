package de.do9fse.winkey.lib.core.model.responses;

@ResponseConfiguration(expectedResponseBytes = 1)
public final class StatusResponseWK2 extends StatusResponse {
    public StatusResponseWK2(
        final boolean waiting,
        final boolean busy,
        final boolean breakIn,
        final boolean xoff,
        final boolean pushButtonStatus,
        final int pushButtonMask
    ) {
        super(waiting, false, busy, breakIn, xoff, pushButtonStatus, pushButtonMask);
    }

    public static StatusResponseWK2 fromData(final byte[] responseBytes) {
        final int value = getStatusByte(responseBytes);
        final boolean isPushButtonStatus = (value & 0b00001000) != 0;
        final int pushButtonMask = isPushButtonStatus
            ? ((value & 0b00010000) != 0 ? 0b1000 : 0) | (value & 0b00000111)
            : 0;

        return new StatusResponseWK2(
            !isPushButtonStatus && (value & 0b00010000) != 0,
            !isPushButtonStatus && (value & 0b00000100) != 0,
            !isPushButtonStatus && (value & 0b00000010) != 0,
            !isPushButtonStatus && (value & 0b00000001) != 0,
            isPushButtonStatus,
            pushButtonMask
        );
    }
}
