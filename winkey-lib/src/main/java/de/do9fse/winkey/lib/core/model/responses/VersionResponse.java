package de.do9fse.winkey.lib.core.model.responses;

import de.do9fse.winkey.lib.core.model.DeviceVersion;
import de.do9fse.winkey.lib.core.model.Validations;

@ResponseConfiguration(expectedResponseBytes = 1)
public record VersionResponse(DeviceVersion version) implements Response {
    public VersionResponse {
        Validations.validateNotNull("Version", version);
    }

    public static VersionResponse fromData(final byte[] responseBytes)  {
        final int responseByte = responseBytes[0] & 0xff;

        final int majorVersion = responseByte / 10;
        final int minorVersion = responseByte % 10;
        return new VersionResponse(
            new DeviceVersion(majorVersion, minorVersion)
        );
    }   
}