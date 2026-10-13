package de.do9fse.winkey.lib;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.error.WinKeyApplicationException;
import de.do9fse.winkey.lib.core.model.error.WKRuntimeException;
import de.do9fse.winkey.lib.core.port.out.Transport;
import de.do9fse.winkey.lib.infrastructure.winkey.transport.serial.SerialTransport;

public final class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static final int ERROR_OPEN = -1;

    private ApplicationContext context;
    private String portName;

    private Transport transport;

    public static void main(final String[] args) throws Exception {
        if (args.length != 1) {
            LOGGER.error("Usage: easy-cw-terminal <serial-port>");
            return;
        }

        final Main application = new Main(args[0]);
        application.run();
    }

    private Main(final String portName) throws Exception {
        this.portName = portName;
    }

    private void run() throws WinKeyApplicationException {
        try {
            start();
            waitForExit();
            stop();
        } catch (final WinKeyApplicationException | RuntimeException | TimeoutException e) {
            LOGGER.error("Application failed", e);
        }
    }

    private void start() throws WinKeyApplicationException, RuntimeException, TimeoutException {
        setRootLogLevel("DEBUG");
        this.context = crateApplicationContext();
        this.transport = createSerialTransport(this.context, this.portName);

        this.transport.open();
        this.transport.initialize(10, TimeUnit.SECONDS);
    }

    private void waitForExit() throws WKRuntimeException {
        try {
            LOGGER.info("Press Enter to exit");
            System.in.read();
        } catch(final IOException e) {
            throw new WKRuntimeException("Failed to read keyboard", e);
        }
    }

    private void stop() throws WinKeyApplicationException {
        if (transport != null) {
            transport.close();
        }
    }

    private ApplicationContext crateApplicationContext() {
        return new ApplicationContext(ProtocolVersion.V2);
    }

    private Transport createSerialTransport(final ApplicationContext context, final String portName) {
        final SerialPort port = SerialPort.getCommPort(portName);
        port.setBaudRate(1200);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        final Transport transport = new SerialTransport(context, port);
        return transport;
    }

    private void setRootLogLevel(String levelName) {
        try {
            final Logger slf4jLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            final Class<?> levelClass = Class.forName("ch.qos.logback.classic.Level");
            
            final Method toLevelMethod = levelClass.getMethod("toLevel", String.class);
            final Object levelObject = toLevelMethod.invoke(null, levelName);

            final Method setLevelMethod = slf4jLogger.getClass().getMethod("setLevel", levelClass);
            setLevelMethod.invoke(slf4jLogger, levelObject);

        } catch (final ReflectiveOperationException e) {
            LOGGER.error("Failed to set log level", e);
        }
    }
}