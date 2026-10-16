package de.do9fse.winkey.lib.core.model.responses;

import java.util.HashMap;
import java.util.Map;

import de.do9fse.winkey.lib.core.model.ApplicationContext;
import de.do9fse.winkey.lib.core.model.Validations;

public class ResponseFactory {
    private Map<Class<? extends Response>, DataInputDeserializer> factories;

    public ResponseFactory(final ApplicationContext context) {
        this.factories = new HashMap<>();

        Validations.validateNotNull("Context", context);
        populateFactories(context);
    }

    public Response createResponse(final Class<? extends Response> responseType, final byte[] responseBytes) {
        final DataInputDeserializer factory = factories.get(responseType);

        if (factory == null) {
            throw new IllegalArgumentException("Factory for type " + responseType + " could not be found");
        }

        return factory.fromData(responseBytes);
    }

    private void populateFactories(final ApplicationContext context) {
        factories.put(CalibrationValueResponse.class, CalibrationValueResponse::fromData);
        factories.put(DefaultsResponse.class, DefaultsResponse::fromData);
        factories.put(EEPROMDumpResponse.class, EEPROMDumpResponse::fromData);
        factories.put(EmptyResponse.class, EmptyResponse::fromData);
        factories.put(EchoResponse.class, EchoResponse::fromData);
        factories.put(PaddleA2DResponse.class, PaddleA2DResponse::fromData);
        factories.put(SpeedPotValueResponse.class, SpeedPotValueResponse::fromData);
        factories.put(SpeedPotentiometerResponse.class, SpeedPotentiometerResponse::fromData);
        factories.put(VersionResponse.class, VersionResponse::fromData);
        factories.put(StatusResponse.class, (byte[] data) -> StatusResponse.fromData(data, context.getProtocolVersion()));
    }
}
