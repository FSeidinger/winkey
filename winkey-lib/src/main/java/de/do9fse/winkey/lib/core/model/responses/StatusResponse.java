package de.do9fse.winkey.lib.core.model.responses;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Status;
import de.do9fse.winkey.lib.core.model.Validations;

@ResponseConfiguration(expectedResponseBytes = 1)
public record StatusResponse(Status status) implements Response {
    public StatusResponse {
        Validations.validateNotNull("Status", status);
    }

    public static StatusResponse fromData(final byte[] responseBytes, final ProtocolVersion protocolVersion ) {
        final int status = Response.toUnsignedInt(responseBytes);
        return new StatusResponse(Status.fromData(status, protocolVersion));
    }
}
