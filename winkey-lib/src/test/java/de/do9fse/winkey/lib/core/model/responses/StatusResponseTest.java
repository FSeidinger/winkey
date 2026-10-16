package de.do9fse.winkey.lib.core.model.responses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.CommonStatus;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Status;
import de.do9fse.winkey.lib.core.model.StatusPushButton;
import de.do9fse.winkey.lib.core.model.StatusV1;
import de.do9fse.winkey.lib.core.model.StatusV2;

@DisplayName("Status response tests")
class StatusResponseTest {

    @Test
    @DisplayName("Test that record holds status reference")
    void holdsStatusReference() {
        final Status status = new StatusV2(new CommonStatus(false, false, false, false));
        final StatusResponse response = new StatusResponse(status);

        assertEquals(status, response.status());
    }

    @Test
    @DisplayName("Test fromData creates status response for protocol version V1")
    void createsStatusResponseForV1() {
        final StatusResponse response = StatusResponse.fromData(new byte[] { (byte) 0xdf }, ProtocolVersion.V1);

        assertNotNull(response);
        assertInstanceOf(StatusV1.class, response.status());
    }

    @Test
    @DisplayName("Test fromData creates status response for protocol version V2")
    void createsStatusResponseForV2() {
        final StatusResponse statusResponse = StatusResponse.fromData(new byte[] { (byte) 0xd7 }, ProtocolVersion.V2);
        assertInstanceOf(StatusV2.class, statusResponse.status());

        final StatusResponse pushButtonResponse = StatusResponse.fromData(new byte[] { (byte) 0xdf }, ProtocolVersion.V2);
        assertInstanceOf(StatusPushButton.class, pushButtonResponse.status());
    }
}
