package de.do9fse.winkey.lib.infrastructure.winkey.transport.serial;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.fazecast.jSerialComm.SerialPort;

import de.do9fse.winkey.lib.core.model.WinKeyContext;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.configuration.serial.BaudRate;
import de.do9fse.winkey.lib.core.model.configuration.serial.DataBits;
import de.do9fse.winkey.lib.core.model.configuration.serial.Parity;
import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;
import de.do9fse.winkey.lib.core.model.configuration.serial.StopBits;
import de.do9fse.winkey.lib.core.model.configuration.serial.Timeout;

class SerialConfigurationAdapterTest {
    @Test
    void appliesConfiguredReadTimeoutToSerialPortInMilliseconds() {
        final WinKeyContext context = new WinKeyContext(ProtocolVersion.V2);
        context.setPortName("/dev/ttyUSB0");
        final SerialConfiguration configuration = new SerialConfiguration(
            BaudRate.DEFAULT,
            DataBits.DEFAULT,
            Parity.DEFAULT,
            StopBits.DEFAULT,
            Timeout.DEFAULT_INITIALIZATION_TIMEOUT,
            new Timeout(2, TimeUnit.SECONDS)
        );
        final SerialPort serialPort = mock(SerialPort.class);

        try (MockedStatic<SerialPort> serialPorts = mockStatic(SerialPort.class)) {
            serialPorts.when(() -> SerialPort.getCommPort("/dev/ttyUSB0")).thenReturn(serialPort);

            new SerialConfigurationAdapter(context, configuration).createSerialPort();

            verify(serialPort).setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 2000, 0);
        }
    }

    @Test
    void roundsSubMillisecondRemainderUpWhenApplyingReadTimeout() {
        final WinKeyContext context = new WinKeyContext(ProtocolVersion.V2);
        context.setPortName("/dev/ttyUSB0");
        final SerialConfiguration configuration = new SerialConfiguration(
            BaudRate.DEFAULT,
            DataBits.DEFAULT,
            Parity.DEFAULT,
            StopBits.DEFAULT,
            Timeout.DEFAULT_INITIALIZATION_TIMEOUT,
            new Timeout(1500, TimeUnit.MICROSECONDS)
        );
        final SerialPort serialPort = mock(SerialPort.class);

        try (MockedStatic<SerialPort> serialPorts = mockStatic(SerialPort.class)) {
            serialPorts.when(() -> SerialPort.getCommPort("/dev/ttyUSB0")).thenReturn(serialPort);

            new SerialConfigurationAdapter(context, configuration).createSerialPort();

            verify(serialPort).setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 2, 0);
        }
    }
}
