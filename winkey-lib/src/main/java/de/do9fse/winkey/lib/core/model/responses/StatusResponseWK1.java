package de.do9fse.winkey.lib.core.model.responses;

@ResponseConfiguration(expectedResponseBytes = 1)
public final class StatusResponseWK1 extends StatusResponse {
    public StatusResponseWK1(
        final boolean waiting,
        final boolean keyDown,
        final boolean busy,
        final boolean breakIn,
        final boolean xoff
    ) {
        super(waiting, keyDown, busy, breakIn, xoff, false, 0);
    }

    public static StatusResponseWK1 fromData(final byte[] responseBytes) {
        final int value = getStatusByte(responseBytes);

        return new StatusResponseWK1(
            (value & 0b00010000) != 0,
            (value & 0b00001000) != 0,
            (value & 0b00000100) != 0,
            (value & 0b00000010) != 0,
            (value & 0b00000001) != 0
        );
    }
}
