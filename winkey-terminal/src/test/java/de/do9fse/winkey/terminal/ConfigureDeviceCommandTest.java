package de.do9fse.winkey.terminal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.jline.shell.CommandSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.configuration.serial.DeviceConfiguration;

public class ConfigureDeviceCommandTest {
    private CommandSession commandSession;

    private ApplicationContext applicationContext;
    private MessageGenerator messageGenerator;

    private ConfigureDeviceCommand command;

    @BeforeEach
    void setUp() {
        applicationContext = new ApplicationContext(WinKeyProtocolVersion.V1);
        messageGenerator = new MessageGenerator();

        commandSession = new CommandSession();
        commandSession.put(Constants.APPLICATION_CONTEXT_KEY, this.applicationContext);
        commandSession.put(Constants.MESSAGE_GENERATOR_KEY, messageGenerator);

        command = new ConfigureDeviceCommand();
    }

    @Test
    void succeedsWithDevice() throws Exception {
        final String[] arguments = new String[]{ "/dev/ttyUSB0"};

        command.execute(commandSession, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT;
        final DeviceConfiguration deviceConfiguration = applicationContext.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }

    @Test
    void succeedsWithCustomBaudRate() throws Exception {
        final String[] arguments = new String[]{ "-b", "1200", "/dev/ttyUSB0"};

        command.execute(commandSession, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT;
        final DeviceConfiguration deviceConfiguration = applicationContext.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }

    @Test
    void succeedsWithInvalidCustomBaudRate() throws Exception {
        final String[] arguments = new String[]{ "-b", "0", "/dev/ttyUSB0"};

        assertThatThrownBy(() -> command.execute(commandSession, arguments))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Baud rate must be greater than zero");
    }

    @Test
    void succeedsWithDefaultBaudRate() throws Exception {
        final String[] arguments = new String[]{ "-d", "default", "/dev/ttyUSB0"};

        command.execute(commandSession, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT;
        final DeviceConfiguration deviceConfiguration = applicationContext.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }

    @Test
    void succeedsWithAlternativeBaudRate() throws Exception {
        final String[] arguments = new String[]{ "-d", "alternative", "/dev/ttyUSB0"};

        command.execute(commandSession, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT_ALTERNATIVE;
        final DeviceConfiguration deviceConfiguration = applicationContext.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }

    @Test
    void failsWithInvalidAlternativeBaudRate() throws Exception {
        final String[] arguments = new String[]{ "-d", "heinz", "/dev/ttyUSB0"};

        assertThatThrownBy(() -> command.execute(commandSession, arguments))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Baud rate must be 'default' or 'alternative'");
    }

    @Test
    void failsWithDeviceMissing() throws Exception {
        final String[] arguments = new String[]{};

        assertThatThrownBy(() -> command.execute(commandSession, arguments))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The <device> parameter is mandatory");

    }
}
