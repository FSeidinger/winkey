package de.do9fse.winkey.lib.core.model.responses;

@ResponseConfiguration()
public record EmptyResponse() implements Response {
    public static EmptyResponse fromData(final byte[] responseBytes) {
        return new EmptyResponse();
    }
}
