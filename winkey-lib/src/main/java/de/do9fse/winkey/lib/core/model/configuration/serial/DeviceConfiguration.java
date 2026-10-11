package de.do9fse.winkey.lib.core.model.configuration.serial;

import de.do9fse.winkey.lib.core.model.Validations;

public record DeviceConfiguration(BaudRate baudRate, DataBits dataBits, Parity parity, StopBits stopBits) {
    public DeviceConfiguration {
        Validations.validateNotNull("Baud rate", baudRate);
        Validations.validateNotNull("Data bits", dataBits);
        Validations.validateNotNull("Parity", parity);
        Validations.validateNotNull("Stop bits", stopBits);
    }

    public static final DeviceConfiguration DEFAULT = new DeviceConfiguration(
        BaudRate.DEFAULT,
        DataBits.DEFAULT,
        Parity.DEFAULT,
        StopBits.DEFAULT
    );

    public static final DeviceConfiguration DEFAULT_ALTERNATIVE = new DeviceConfiguration(
        BaudRate.DEFAULT_ALTERNATIVE,
        DataBits.DEFAULT,
        Parity.DEFAULT,
        StopBits.DEFAULT
    );
}
