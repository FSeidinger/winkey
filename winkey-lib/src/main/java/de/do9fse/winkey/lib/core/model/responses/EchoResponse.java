package de.do9fse.winkey.lib.core.model.responses;

@ResponseConfiguration(
    expectedResponseBytes = 1
)
public record EchoResponse(char echoChar) implements Response {
    public static EchoResponse fromData(final byte[] responseBytes) {
        return new EchoResponse(Response.toChar(responseBytes));
    }
}
