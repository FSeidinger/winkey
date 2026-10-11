package de.do9fse.winkey.lib.core.model.configuration.serial;

public record DataBits(int value) {
    public static DataBits DEFAULT = new DataBits(8);

    public DataBits {
        if (value <= 0 || value > 8) {
            throw new IllegalArgumentException("Data bits must be between one and eight");
        }
    }
}
