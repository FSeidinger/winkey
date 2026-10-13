package de.do9fse.winkey.lib.core.model.responses;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class ResponseFactoryTest {
    private final ResponseFactory responseFactory = new ResponseFactory();

    @Test
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
            VersionResponse.class, new byte[] { 10 }
        );

        responses.forEach((responseType, responseBytes) ->
            assertInstanceOf(responseType, responseFactory.createResponse(responseType, responseBytes))
        );
    }

    @Test
    void rejectsUnregisteredResponseTypes() {
        assertThrows(
            IllegalArgumentException.class,
            () -> responseFactory.createResponse(Response.class, new byte[0])
        );
    }

    @Test
    void createsStatusResponseForTheRequestedWkMode() {
        final StatusResponse wk1Response = assertInstanceOf(
            StatusResponseWK1.class,
            responseFactory.createResponse(new byte[] { (byte) 0xdf }, false)
        );
        final StatusResponse wk2Response = assertInstanceOf(
            StatusResponseWK2.class,
            responseFactory.createResponse(new byte[] { (byte) 0xdf }, true)
        );

        assertTrue(wk1Response.keyDown());
        assertTrue(wk2Response.pushButtonStatus());
    }
}
