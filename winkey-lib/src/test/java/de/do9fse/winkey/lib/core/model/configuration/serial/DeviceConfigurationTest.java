package de.do9fse.winkey.lib.core.model.configuration.serial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class DeviceConfigurationTest {
    private DeviceConfiguration defaultDeviceConfiguration = new DeviceConfiguration(
        BaudRate.DEFAULT,
        DataBits.DEFAULT,
        Parity.DEFAULT,
        StopBits.DEFAULT,
        Timeout.DEFAULT_INITIALIZATION_TIMEOUT,
        Timeout.DEFAULT_READ_TIMEOUT
    );

    private DeviceConfiguration alternativeDeviceConfiguration = new DeviceConfiguration(
        BaudRate.DEFAULT_ALTERNATIVE,
        DataBits.DEFAULT,
        Parity.DEFAULT,
        StopBits.DEFAULT,
        Timeout.DEFAULT_INITIALIZATION_TIMEOUT,
        Timeout.DEFAULT_READ_TIMEOUT
    );

    @Test 
    void assertDefault() {
        final DeviceConfiguration expectedDefaultDeviceConfiguration = defaultDeviceConfiguration;

        final DeviceConfiguration actualDefaultConfiguration = DeviceConfiguration.DEFAULT;
        assertThat(actualDefaultConfiguration).isEqualTo(expectedDefaultDeviceConfiguration);

        final DeviceConfiguration expectedAlternativeDefaultDeviceConfiguration = alternativeDeviceConfiguration;
        final DeviceConfiguration actualAlternativeDefaultConfiguration = DeviceConfiguration.DEFAULT_ALTERNATIVE;
        assertThat(actualAlternativeDefaultConfiguration).isEqualTo(expectedAlternativeDefaultDeviceConfiguration);
    }

    @Test
    void acceptsValidParameters() {
        final DeviceConfiguration deviceConfiguration = new DeviceConfiguration(
            BaudRate.DEFAULT,
            DataBits.DEFAULT,
            Parity.DEFAULT,
            StopBits.DEFAULT,
            Timeout.DEFAULT_INITIALIZATION_TIMEOUT,
            Timeout.DEFAULT_READ_TIMEOUT
        );
        final DeviceConfiguration expectedConfiguration = DeviceConfiguration.DEFAULT;
        assertThat(deviceConfiguration).isEqualTo(expectedConfiguration);
    }

    @Test
    void rejectsNullParameters() {
        assertThatThrownBy(() -> new DeviceConfiguration(null, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Baud rate must not be null");
            
        assertThatThrownBy(() -> new DeviceConfiguration(BaudRate.DEFAULT, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Data bits must not be null");

        assertThatThrownBy(() -> new DeviceConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Parity must not be null");

        assertThatThrownBy(() -> new DeviceConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, Parity.DEFAULT, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Stop bits must not be null");

        assertThatThrownBy(() -> new DeviceConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, Parity.DEFAULT, StopBits.DEFAULT, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Initialization timeout must not be null");

        assertThatThrownBy(() -> new DeviceConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, Parity.DEFAULT, StopBits.DEFAULT, Timeout.DEFAULT_INITIALIZATION_TIMEOUT, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Read timeout must not be null");
      }
}