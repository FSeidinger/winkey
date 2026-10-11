package de.do9fse.winkey.lib.core.model;

import java.util.Objects;
import java.util.Optional;

import de.do9fse.winkey.lib.core.model.configuration.serial.DeviceConfiguration;
import de.do9fse.winkey.lib.core.port.out.WinKeyJobQueue;
import de.do9fse.winkey.lib.core.port.out.WinKeyTransport;
import de.do9fse.winkey.lib.infrastructure.winkey.DefaultWinKeyJobQueue;

public class ApplicationContext {
    private final WinKeyJobQueue jobQueue;
    private final ApplicationStateMachine stateMachine;

    private WinKeyProtocolVersion version;
    private Optional<String> portName;
    private DeviceConfiguration deviceConfiguration;
    private WinKeyTransport transport;

    public ApplicationContext(final WinKeyProtocolVersion version) {
        this.version = Objects.requireNonNull(version, "Version must not be null");
        this.portName = Optional.empty();
        this.deviceConfiguration = DeviceConfiguration.DEFAULT;
        this.stateMachine = new ApplicationStateMachine();
        this.jobQueue = new DefaultWinKeyJobQueue();
    }

    public void transitionToState(final WinKeyState newState) {
        this.stateMachine.transitionToState(this, newState);
    }

    public WinKeyState getState() {
        return stateMachine.getState();
    }
   
    public WinKeyJobQueue getJobQueue() {
        return jobQueue;
    }

    public void setVersion(final WinKeyProtocolVersion version) {
        this.version = Objects.requireNonNull(version, "Version must not be null");
    }

    public WinKeyProtocolVersion getVersion() {
        return version;
    }
    
    public void setPortName(final String portName) {
        this.portName = Optional.of(Validations.validateNotBlank("Port name", portName));
    }

    public String getPortName() {
        return this.portName.get();
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return deviceConfiguration;
    }

    public void setDeviceConfiguration(final DeviceConfiguration deviceConfiguration) {
        if (stateMachine.getState() != WinKeyState.CLOSED) {
            throw new IllegalStateException("Device configuration can only be set if device is closed");
        }

        this.deviceConfiguration = Objects.requireNonNull(deviceConfiguration, "Device configuration must not be null");
    }

    public WinKeyTransport getTransport() {
        return transport;
    }

    public void setTransport(final WinKeyTransport transport) {
        if (this.stateMachine.getState() != WinKeyState.CLOSED) {
            throw new IllegalStateException("Transport is already running");
        }

        this.transport = transport;
    }
}
