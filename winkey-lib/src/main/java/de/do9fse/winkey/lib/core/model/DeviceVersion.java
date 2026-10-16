package de.do9fse.winkey.lib.core.model;

import java.text.MessageFormat;

public record DeviceVersion(int majorVersion, int minorVersion) {
    public static final int MAJOR_VERSION_MIN = 1;
    public static final int MAJOR_VERSION_MAX = 3;
    public static final int MINOR_VERSION_MIN = 0;

    public DeviceVersion {
        if (majorVersion < MAJOR_VERSION_MIN || majorVersion > MAJOR_VERSION_MAX) {
            final String message = MessageFormat.format(
                "WinKey major version must be between {0} and {1} but was {2}",
                MAJOR_VERSION_MIN,
                MAJOR_VERSION_MAX,
                majorVersion
            );

            throw new IllegalArgumentException(message);
        }

        if (minorVersion < MINOR_VERSION_MIN) {
            final String message = MessageFormat.format(
                "Minor version must not be less than {0} but was {1}",
                MINOR_VERSION_MIN,
                minorVersion
            );
            
            throw new IllegalArgumentException(message);
        }
    }

    public String getVersionString() {
        return "v" + majorVersion + "." + minorVersion;
    }

    public ProtocolVersion toProtocolVersion() {
        return switch (majorVersion) {
            case 1 -> ProtocolVersion.V1;
            case 2 -> ProtocolVersion.V2;
            case 3 -> ProtocolVersion.V3;

            // Should not happen
            default -> throw new IllegalArgumentException("Unknown major version " + majorVersion);
        };
    }

    public static DeviceVersion fromProtocolVersion(final ProtocolVersion protocolVersion) {
        return switch (protocolVersion) {
            case V1 -> new DeviceVersion(1, MINOR_VERSION_MIN);
            case V2 -> new DeviceVersion(2, MINOR_VERSION_MIN);
            case V3 -> new DeviceVersion(3, MINOR_VERSION_MIN);

            // Should not happen
            default -> throw new IllegalArgumentException("Unknown protocol version " + protocolVersion);
        };
    }

    public static DeviceVersion parse(final String version) {
        final String normalizedVersion = Validations
            .validateNotBlank("Version", version)
            .toLowerCase()
            .replace("v", "");
        
        final String[] parts = normalizedVersion.split("\\.", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException(MessageFormat.format("Version must given in the notation [v|V]x.y: {0}", version));
        }

        final String majorVersion = Validations.validateNotBlank("Major version", parts[0]);
        final String minorVersion = Validations.validateNotBlank("Minor version", parts[1]);

        try {
            return new DeviceVersion(
                Integer.parseInt(majorVersion),
                Integer.parseInt(minorVersion)
            );
        } catch (final NumberFormatException exception) {
            throw new IllegalArgumentException("Either major or minor version is not an integer", exception);
        }
    }
}
