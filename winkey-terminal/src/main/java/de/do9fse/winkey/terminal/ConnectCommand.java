package de.do9fse.winkey.terminal;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jline.shell.CommandSession;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.WinKeyState;
import de.do9fse.winkey.lib.core.model.configuration.serial.DeviceConfiguration;
import de.do9fse.winkey.lib.core.model.error.WinKeyApplicationException;
import de.do9fse.winkey.lib.core.port.out.WinKeyTransport;
import de.do9fse.winkey.lib.infrastructure.winkey.transport.serial.SerialConfigurationAdapter;
import de.do9fse.winkey.lib.infrastructure.winkey.transport.serial.WinKeySerialTransport;

public class ConnectCommand extends TerminalCommand {
    protected ConnectCommand() {
        super("connect", "co" );
    }

    @Override
    public String description() {
        return "Connect to a WinKey device";
    }

    @Override
    public Object execute(final CommandSession session, String[] args) throws Exception {
        evaluateContext(session);

        if (args.length != 0) {
            throw new IllegalArgumentException("Error: the connect command does not support parameters");
        }

        final ApplicationContext applicationContext = Constants.getApplicationContext(session);
        if (applicationContext.getState() != WinKeyState.CLOSED) {
            throw new IllegalStateException(generator.createErrorMessage(Errors.DEVICE_ALREADY_CLOSED, applicationContext));
        } 

        final DeviceConfiguration deviceConfiguration = applicationContext.getDeviceConfiguration();

        try {
            final SerialPort serialPort = createSerialPort(stderr, applicationContext, deviceConfiguration);
            final WinKeyTransport transport = openAndInitializeTransport(stderr, applicationContext, serialPort);
            applicationContext.setTransport(transport);           
            stdout.println(MessageGenerator.successfullyOpenedDevice(applicationContext));
        } catch (final SerialPortInvalidPortException e) {
            throw new IllegalArgumentException(generator.createErrorMessage(Errors.INVALID_PORT, applicationContext));
        } catch (final NoSuchElementException e) {
            throw new IllegalStateException(generator.createErrorMessage(Errors.NO_PORT_SET, applicationContext), e);
        }

        final String message = MessageFormat.format("Successfully connected to device {0}", applicationContext.getPortName());
        stdout.println(message);

        return null;
    }

    private SerialPort createSerialPort(final PrintStream stderr, final ApplicationContext context, final DeviceConfiguration deviceConfiguration) throws SerialPortInvalidPortException {
        final SerialConfigurationAdapter adapter = new SerialConfigurationAdapter(context, deviceConfiguration);
        return adapter.createSerialPort();
    }

    private WinKeyTransport openAndInitializeTransport(final PrintStream stderr, final ApplicationContext context, final SerialPort serialPort) throws WinKeyApplicationException, TimeoutException {
        WinKeyTransport transport = null;

        try {
            transport = new WinKeySerialTransport(context, serialPort);
            transport.open();
            transport.initialize(10, TimeUnit.SECONDS);
        } finally {
            if (context.getState() != WinKeyState.CLOSED) {
                closeTransport(transport);
            }
        }

        return transport;
    }

    private void closeTransport(final WinKeyTransport transport) {
        try {
            transport.close();
        } catch (final WinKeyApplicationException e) {
            // Silently swallow
        }
    }
}
