package de.do9fse.winkey.lib.core.model.responses;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.StatusPushButton;
import de.do9fse.winkey.lib.core.model.StatusV1;
import de.do9fse.winkey.lib.core.model.StatusV2;

@DisplayName("Response factory tests")
class ResponseFactoryTest {
    private ApplicationContext context;
    private ResponseFactory responseFactory;

    @BeforeEach
    void setUp() {
        context = new ApplicationContext(ProtocolVersion.V2);
        responseFactory = new ResponseFactory(context);
    }

    @Test
    @DisplayName("Test that null application context is rejected")
    void rejectsNullContext() {
        assertThrows(
            NullPointerException.class,
            () -> new ResponseFactory(null)
        );
    }

    @Test
    @DisplayName("Test creating every supported response type")
    void createsEverySupportedResponseType() {
        final Map<Class<? extends Response>, byte[]> responses = Map.of(
            CalibrationValueResponse.class, new byte[] { (byte) 0xff },
            DefaultsResponse.class, new byte[] { 0x31, 25, 4, 50, 12, 25, 10, 20, 50, 7, 18, 55, 50, (byte) 0b10011011, (byte) 0xff },
            EEPROMDumpResponse.class, new byte[EEPROMDumpResponse.DATA_LENGTH],
            EmptyResponse.class, new byte[0],
            EchoResponse.class, new byte[] { 'K' },
            PaddleA2DResponse.class, new byte[] { 0 },
            SpeedPotValueResponse.class, new byte[] { (byte) 0x80 },
            SpeedPotentiometerResponse.class, new byte[] { 0 },
            VersionResponse.class, new byte[] { 10 },
            StatusResponse.class, new byte[] { (byte) 0xc0 }
        );

        responses.forEach((responseType, responseBytes) ->
            assertInstanceOf(responseType, responseFactory.createResponse(responseType, responseBytes))
        );
    }

    @Test
    @DisplayName("Test that unregistered response types are rejected")
    void rejectsUnregisteredResponseTypes() {
        assertThrows(
            IllegalArgumentException.class,
            () -> responseFactory.createResponse(Response.class, new byte[0])
        );
    }

    @Test
    @DisplayName("Test creating StatusResponse for protocol version V1")
    void createsStatusResponseForProtocolVersionV1() {
        context.setDeviceVersion(ProtocolVersion.V1.toDeviceVersion());
        final Response response = responseFactory.createResponse(StatusResponse.class, new byte[] { (byte) 0xdf });
        final StatusResponse statusResponse = assertInstanceOf(StatusResponse.class, response);
        final StatusV1 status = assertInstanceOf(StatusV1.class, statusResponse.status());
        assertTrue(status.keyDown());
    }

    @Test
    @DisplayName("Test creating StatusResponse for protocol version V2 with push button")
    void createsStatusResponseForProtocolVersionV2WithPushButton() {
        context.setDeviceVersion(ProtocolVersion.V2.toDeviceVersion());
        final Response response = responseFactory.createResponse(StatusResponse.class, new byte[] { (byte) 0xdf });
        final StatusResponse statusResponse = assertInstanceOf(StatusResponse.class, response);
        final StatusPushButton status = assertInstanceOf(StatusPushButton.class, statusResponse.status());
        assertTrue(status.pb1Status());
    }

    @Test
    @DisplayName("Test creating StatusResponse for protocol version V2 without push button")
    void createsStatusResponseForProtocolVersionV2WithoutPushButton() {
        context.setDeviceVersion(ProtocolVersion.V2.toDeviceVersion());
        final Response response = responseFactory.createResponse(StatusResponse.class, new byte[] { (byte) 0xd7 });
        final StatusResponse statusResponse = assertInstanceOf(StatusResponse.class, response);
        final StatusV2 status = assertInstanceOf(StatusV2.class, statusResponse.status());
        assertTrue(status.commonStatus().waitFlag());
    }
}
