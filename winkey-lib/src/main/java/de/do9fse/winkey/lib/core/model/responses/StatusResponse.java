package de.do9fse.winkey.lib.core.model.responses;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Status;

@ResponseConfiguration(expectedResponseBytes = 1)
public record StatusResponse(Status status) implements Response {
    public static StatusResponse fromData(final byte[] responseBytes, final ProtocolVersion protocolVersion ) {
        final int status = responseBytes[0] & 0xff;
        return new StatusResponse(Status.fromData(status, protocolVersion));
    }
}
