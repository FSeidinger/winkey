package de.do9fse.winkey.lib.infrastructure.winkey.transport.serial;

import java.util.concurrent.TimeUnit;

import com.fazecast.jSerialComm.SerialPort;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.serial.Timeout;

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
        serialPort.setComPortTimeouts(
            SerialPort.TIMEOUT_READ_BLOCKING,
            toMilliseconds(deviceConfiguration.readTimeout()),
            0
        );

        return serialPort;
    }

    private int toMilliseconds(final Timeout timeout) {
        long timeoutMillis = timeout.timeUnit().toMillis(timeout.timeout());
        if (timeoutMillis > Integer.MAX_VALUE) {
            throw new ArithmeticException("Serial read timeout exceeds the supported millisecond range");
        }

        if (timeout.timeUnit().toNanos(timeout.timeout()) > TimeUnit.MILLISECONDS.toNanos(timeoutMillis)) {
            timeoutMillis++;
        }

        if (timeoutMillis < 1) {
            throw new IllegalArgumentException("Serial read timeout must be at least one millisecond");
        }

        return Math.toIntExact(timeoutMillis);
    }
}
