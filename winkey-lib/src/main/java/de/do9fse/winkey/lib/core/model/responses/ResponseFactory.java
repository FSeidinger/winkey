package de.do9fse.winkey.lib.core.model.responses;

import java.util.HashMap;
import java.util.Map;

public class ResponseFactory {
    private Map<Class<? extends Response>, DataInputDeserializer> factories = new HashMap<>() {{
        put(CalibrationValueResponse.class, CalibrationValueResponse::fromData);
        put(DefaultsResponse.class, DefaultsResponse::fromData);
        put(EEPROMDumpResponse.class, EEPROMDumpResponse::fromData);
        put(EmptyResponse.class, EmptyResponse::fromData);
        put(EchoResponse.class, EchoResponse::fromData);
        put(PaddleA2DResponse.class, PaddleA2DResponse::fromData);
        put(SpeedPotValueResponse.class, SpeedPotValueResponse::fromData);
        put(SpeedPotentiometerResponse.class, SpeedPotentiometerResponse::fromData);
        put(VersionResponse.class, VersionResponse::fromData);
    }};

    public Response createResponse(final Class<? extends Response> responseType, final byte[] responseBytes) {
        final DataInputDeserializer factory = factories.get(responseType);

        if (factory == null) {
            throw new IllegalArgumentException("Factory for type " + responseType + " could not be found");
        }

        return factory.fromData(responseBytes);
    }

    public StatusResponse createResponse(final byte[] responseBytes, final boolean wk2Mode) {
        return wk2Mode
            ? StatusResponseWK2.fromData(responseBytes)
            : StatusResponseWK1.fromData(responseBytes);
    }
}
