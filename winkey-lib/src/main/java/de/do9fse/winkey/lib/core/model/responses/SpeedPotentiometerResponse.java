package de.do9fse.winkey.lib.core.model.responses;

@ResponseConfiguration(expectedResponseBytes = 1)
public record SpeedPotentiometerResponse(int value) implements Response {
    public static SpeedPotentiometerResponse fromData(final byte[] responseBytes) {
        return new SpeedPotentiometerResponse(Response.toUnsignedInt(responseBytes));
    }
}
