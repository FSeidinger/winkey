package de.do9fse.winkey.lib.core.model.configuration.serial;

import de.do9fse.winkey.lib.core.model.Validations;

public record BaudRate(int value) {
    public static final BaudRate DEFAULT = new BaudRate(1200);
    public static final BaudRate DEFAULT_ALTERNATIVE = new BaudRate(9600);

    public static final String DEFAULT_KEY = "default" ;
    public static final String DEFAULT_ALTERNATIVE_KEY = "alternative" ;

    public BaudRate{
        if (value <= 0) {
            throw new IllegalArgumentException("Baud rate must be greater than zero");
        }
    }

    public static BaudRate parseCustomBaudRate(final String defaultBaudRateValue) {
        Validations.validateNotNull("Baud rate value", defaultBaudRateValue);
        final int baudRate = Integer.parseInt(defaultBaudRateValue);
        return new BaudRate(baudRate);
    }

    public static BaudRate parseDefaultBaudRate(final String defaultBaudRateValue) {
        switch (defaultBaudRateValue) {
            case DEFAULT_KEY:
                return DEFAULT;

            case DEFAULT_ALTERNATIVE_KEY:
                return DEFAULT_ALTERNATIVE;

            default: throw new IllegalArgumentException("Baud rate must be '"+ DEFAULT_KEY + "' or '" + DEFAULT_ALTERNATIVE_KEY + "'");
        }
    }
}
