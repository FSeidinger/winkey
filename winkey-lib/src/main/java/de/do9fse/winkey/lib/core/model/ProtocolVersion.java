package de.do9fse.winkey.lib.core.model;

public enum ProtocolVersion {
    V1(1),
    V2(2),
    V3(3);

    private final int protocolMajorVersion;

    private ProtocolVersion(final int majorVersion) {
        this.protocolMajorVersion = majorVersion;
    }

    public int getProtocolMajorVersion() {
        return protocolMajorVersion;
    }

    public static ProtocolVersion fromMajorVersion(final int majorVersion) {
        for (final ProtocolVersion version : values()) {
            if (version.protocolMajorVersion == majorVersion) {
                return version;
            }
        }

        throw new IllegalArgumentException("Unsupported protocol major version: " + majorVersion);
    }
}

