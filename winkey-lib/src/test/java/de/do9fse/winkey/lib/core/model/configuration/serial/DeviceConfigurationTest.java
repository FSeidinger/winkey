package de.do9fse.winkey.lib.core.model.configuration.serial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class DeviceConfigurationTest {
    @Test 
    void assertDefault() {
        final DeviceConfiguration expectedDefaultDeviceConfiguration = new DeviceConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, Parity.DEFAULT, StopBits.DEFAULT);
        final DeviceConfiguration actualDefaultConfiguration = DeviceConfiguration.DEFAULT;
        assertThat(actualDefaultConfiguration).isEqualTo(expectedDefaultDeviceConfiguration);

        final DeviceConfiguration expectedAlternativeDefaultDeviceConfiguration = new DeviceConfiguration(BaudRate.DEFAULT_ALTERNATIVE, DataBits.DEFAULT, Parity.DEFAULT, StopBits.DEFAULT);
        final DeviceConfiguration actualAlternativeDefaultConfiguration = DeviceConfiguration.DEFAULT_ALTERNATIVE;
        assertThat(actualAlternativeDefaultConfiguration).isEqualTo(expectedAlternativeDefaultDeviceConfiguration);
    }

    @Test
    void acceptsValidParameters() {
        final DeviceConfiguration deviceConfiguration = new DeviceConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, Parity.DEFAULT, StopBits.DEFAULT);
        final DeviceConfiguration expectedConfiguration = DeviceConfiguration.DEFAULT;
        assertThat(deviceConfiguration).isEqualTo(expectedConfiguration);
    }

    @Test
    void rejectsNullParameters() {
        assertThatThrownBy(() -> new DeviceConfiguration(null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Baud rate must not be null");
            
        assertThatThrownBy(() -> new DeviceConfiguration(BaudRate.DEFAULT, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Data bits must not be null");

        assertThatThrownBy(() -> new DeviceConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Parity must not be null");

        assertThatThrownBy(() -> new DeviceConfiguration(BaudRate.DEFAULT, DataBits.DEFAULT, Parity.DEFAULT, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Stop bits must not be null");
        }
    }
