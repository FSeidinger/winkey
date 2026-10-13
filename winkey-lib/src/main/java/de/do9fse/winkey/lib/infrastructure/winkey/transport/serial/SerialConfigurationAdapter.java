package de.do9fse.winkey.lib.infrastructure.winkey.transport.serial;

import com.fazecast.jSerialComm.SerialPort;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;

public class SerialConfigurationAdapter {
    private final ApplicationContext context;
    private final SerialConfiguration deviceConfiguration;

    public SerialConfigurationAdapter(final ApplicationContext context, final SerialConfiguration deviceConfiguration) {
        this.context = Validations.validateNotNull("Application context", context);
        this.deviceConfiguration = Validations.validateNotNull("Device configuration", deviceConfiguration);
    }

    public SerialPort createSerialPort() {
        final String portName = context.getPortName();
        final SerialPort serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(deviceConfiguration.baudRate().value());
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 500, 0);

        return serialPort;
    }
}
