package de.do9fse.winkey.terminal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.configuration.serial.DeviceConfiguration;
import de.do9fse.winkey.terminal.test.tools.AbstractCommandTest;
import de.do9fse.winkey.terminal.test.tools.ArgumentsBuilder;

public class ConfigureDeviceCommandTest extends AbstractCommandTest {
    /**
     * The subject under test
     */
    private ConfigureDeviceCommand command;

    @BeforeEach
    void setUp() {
        command = new ConfigureDeviceCommand();
    }

    @Test
    void assertThatConfigurationSucceedsWithDevice() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addArgument("/dev/ttyUSB0")
            .build();

        command.execute(session, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT;
        final DeviceConfiguration deviceConfiguration = context.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }

    @Test
    void assertThatConfigurationSucceedsWithCustomBaudRate() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addOption("-b", "1200")
            .addArgument("/dev/ttyUSB0")
            .build();

        command.execute(session, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT;
        final DeviceConfiguration deviceConfiguration = context.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }

    @Test
    void assertThatConfigurationFailsWithInvalidCustomBaudRate() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addOption("-b", "0")
            .addArgument("/dev/ttyUSB0")
            .build();

        assertThatThrownBy(() -> command.execute(session, arguments))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Baud rate must be greater than zero");
    }

    @Test
    void assertThatConfigurationSucceedsWithDefaultBaudRate() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addOption("-d", "default")
            .addArgument("/dev/ttyUSB0")
            .build();

        command.execute(session, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT;
        final DeviceConfiguration deviceConfiguration = context.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }

    @Test
    void assertThatConfigurationSucceedsWithAlternativeBaudRate() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addOption("-d", "alternative")
            .addArgument("/dev/ttyUSB0")
            .build();

        command.execute(session, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT_ALTERNATIVE;
        final DeviceConfiguration deviceConfiguration = context.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }

    @Test
    void assertThatConfigurationFailsWithInvalidAlternativeBaudRate() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addOption("-d", "heinz")
            .addArgument("/dev/ttyUSB0")
            .build();

        assertThatThrownBy(() -> command.execute(session, arguments))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Baud rate must be 'default' or 'alternative'");
    }

    @Test
    void assertThatConfigurationFailsWithMissingDevice() throws Exception {
        final String[] arguments = ArgumentsBuilder.EMPTY;

        assertThatThrownBy(() -> command.execute(session, arguments))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The <device> parameter is mandatory");

    }

    @Test
    void assertThatConfigurationSucceedsWithBaudRateLongOption() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addLongOption("--baudRate", "1200")
            .addArgument("/dev/ttyUSB0")
            .build();

        command.execute(session, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT;
        final DeviceConfiguration deviceConfiguration = context.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }

    @Test
    void assertThatConfigurationSucceedsWithDefaultBaudRateLongOption() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addLongOption("--defaultBaudRate", "default")
            .addArgument("/dev/ttyUSB0")
            .build();

        command.execute(session, arguments);

        final DeviceConfiguration expectedDeviceConfiguration = DeviceConfiguration.DEFAULT;
        final DeviceConfiguration deviceConfiguration = context.getDeviceConfiguration();
        assertThat(deviceConfiguration).isEqualTo(expectedDeviceConfiguration);
    }
}
