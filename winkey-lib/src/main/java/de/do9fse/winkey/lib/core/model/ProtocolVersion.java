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
}
