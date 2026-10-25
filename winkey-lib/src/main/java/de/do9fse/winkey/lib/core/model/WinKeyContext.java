package de.do9fse.winkey.lib.core.model;

import java.util.Optional;

import de.do9fse.winkey.lib.core.model.configuration.serial.SerialConfiguration;
import de.do9fse.winkey.lib.core.port.out.JobQueue;
import de.do9fse.winkey.lib.core.port.out.Transport;
import de.do9fse.winkey.lib.infrastructure.winkey.DefaultJobQueue;

public class WinKeyContext {
    private final JobQueue jobQueue;
    private final WinKeyStateMachine stateMachine;

    private WinKeyConfiguration appConfiguration;
    private DeviceVersion deviceVersion;
    private ProtocolVersion protocolVersion;

    private Optional<String> portName;
    private SerialConfiguration deviceConfiguration;
    
    private Transport transport;

    public WinKeyContext(final ProtocolVersion version) {
        this.jobQueue = new DefaultJobQueue();
        this.stateMachine = new WinKeyStateMachine();
        this.appConfiguration = WinKeyConfiguration.DEFAULT;

        this.setDeviceVersion(Validations.validateNotNull("Version", version).toDeviceVersion());

        this.portName = Optional.empty();
        this.deviceConfiguration = SerialConfiguration.DEFAULT;
    }

    public void transitionToState(final WinKeyState newState) {
        this.stateMachine.transitionToState(this, newState);
    }

    public WinKeyState getState() {
        return stateMachine.getState();
    }
   
    public JobQueue getJobQueue() {
        return jobQueue;
    }

    public void setAppConfiguration(WinKeyConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    public WinKeyConfiguration getAppConfiguration() {
        return appConfiguration;
    }

    public void setDeviceVersion(final DeviceVersion deviceVersion) {
        this.deviceVersion = Validations.validateNotNull("Device version", deviceVersion);
        this.protocolVersion = deviceVersion.toProtocolVersion();
    }

    public DeviceVersion getDeviceVersion() {
        return deviceVersion;
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setPortName(final String portName) {
        this.portName = Optional.of(Validations.validateNotBlank("Port name", portName));
    }

    public String getPortName() {
        return this.portName.get();
    }

    public SerialConfiguration getDeviceConfiguration() {
        return deviceConfiguration;
    }

    public void setDeviceConfiguration(final SerialConfiguration deviceConfiguration) {
        if (stateMachine.getState() != WinKeyState.CLOSED) {
            throw new IllegalStateException("Device configuration can only be set if device is closed");
        }

        this.deviceConfiguration = Validations.validateNotNull("Device configuration", deviceConfiguration);
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(final Transport transport) {
        if (this.stateMachine.getState() != WinKeyState.CLOSED) {
            throw new IllegalStateException("Transport is already running");
        }

        this.transport = transport;
    }
}
