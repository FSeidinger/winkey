package de.do9fse.winkey.lib.core.model.responses;

@ResponseConfiguration(expectedResponseBytes = 1)
public record CalibrationValueResponse(int value) implements Response {
    public static CalibrationValueResponse fromData(final byte[] responseBytes) {
        return new CalibrationValueResponse(Byte.toUnsignedInt(responseBytes[0]));
    }
}
