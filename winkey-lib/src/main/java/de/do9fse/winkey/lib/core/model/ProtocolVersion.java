package de.do9fse.winkey.lib.core.model;

public enum ProtocolVersion {
    V1,
    V2,
    V3;

    public DeviceVersion toDeviceVersion() {
        return switch (this) {
            case V1 -> new DeviceVersion(1, DeviceVersion.MINOR_VERSION_MIN);
            case V2 -> new DeviceVersion(2, DeviceVersion.MINOR_VERSION_MIN);
            case V3 -> new DeviceVersion(3, DeviceVersion.MINOR_VERSION_MIN);
        };
    }
}
