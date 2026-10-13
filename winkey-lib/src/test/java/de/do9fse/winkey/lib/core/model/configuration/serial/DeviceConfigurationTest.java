package de.do9fse.winkey.lib.core.model.configuration.serial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class DeviceConfigurationTest {
    private SerialConfiguration defaultDeviceConfiguration = new SerialConfiguration(
        BaudRate.DEFAULT,
        DataBits.DEFAULT,
        Parity.DEFAULT,
        StopBits.DEFAULT,
        Timeout.DEFAULT_INITIALIZATION_TIMEOUT,
        Timeout.DEFAULT_READ_TIMEOUT
    );

    private SerialConfiguration alternativeDeviceConfiguration = new SerialConfiguration(
        BaudRate.DEFAULT_ALTERNATIVE,
        DataBits.DEFAULT,
        Parity.DEFAULT,
        StopBits.DEFAULT,
        Timeout.DEFAULT_INITIALIZATION_TIMEOUT,
        Timeout.DEFAULT_READ_TIMEOUT
    );

    @Test 
    void assertDefault() {
        final SerialConfiguration expectedDefaultDeviceConfiguration = defaultDeviceConfiguration;

        final SerialConfiguration actualDefaultConfiguration = SerialConfiguration.DEFAULT;
        assertThat(actualDefaultConfiguration).isEqualTo(expectedDefaultDeviceConfiguration);

        final SerialConfiguration expectedAlternativeDefaultDeviceConfiguration = alternativeDeviceConfiguration;
        final SerialConfiguration actualAlternativeDefaultConfiguration = SerialConfiguration.DEFAULT_ALTERNATIVE;
        assertThat(actualAlternativeDefaultConfiguration).isEqualTo(expectedAlternativeDefaultDeviceConfiguration);
    }

    @Test
    void acceptsValidParameters() {
        final SerialConfiguration deviceConfiguration = new SerialConfiguration(
            BaudRate.DEFAULT,
            DataBits.DEFAULT,
            Parity.DEFAULT,
            StopBits.DEFAULT,
            Timeout.DEFAULT_INITIALIZATION_TIMEOUT,
            Timeout.DEFAULT_READ_TIMEOUT
        );
        final SerialConfiguration expectedConfiguration = SerialConfiguration.DEFAULT;
        assertThat(deviceConfiguration).isEqualTo(expectedConfiguration);
    }

    @Test
    void rejectsNullParameters() {
        assertThatThrownBy(() -> new SerialConfiguration(null, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Baud rate must not be null");
            
        assertThatThrownBy(() -> new SerialConfiguration(BaudRate.DEFAULT, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Data bits must not be null");

        assertThatThrownBy(() -> new SerialConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Parity must not be null");

        assertThatThrownBy(() -> new SerialConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, Parity.DEFAULT, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Stop bits must not be null");

        assertThatThrownBy(() -> new SerialConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, Parity.DEFAULT, StopBits.DEFAULT, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Initialization timeout must not be null");

        assertThatThrownBy(() -> new SerialConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, Parity.DEFAULT, StopBits.DEFAULT, Timeout.DEFAULT_INITIALIZATION_TIMEOUT, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Read timeout must not be null");
      }
}