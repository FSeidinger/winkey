package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import java.text.MessageFormat;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.HostModeCommand;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record SetBufferPointerCommand(Operation operation, int positionOrNullCount) implements HostModeCommand {
    private static final int RESET_VALUE = -1;

    public enum Operation {
        RESET,
        OVERWRITE,
        INSERT,
        ADD_NULLS
    }

    public SetBufferPointerCommand {
        Validations.validateNotNull("Buffer pointer operation", operation);
        
        if (operation == Operation.RESET) {
            if (positionOrNullCount != RESET_VALUE) {
                final String message = MessageFormat.format(
                    "Reset operation requires position or null count {0} but was {1}",
                    RESET_VALUE,
                    positionOrNullCount
                );
                throw new IllegalArgumentException(message);
            }
        } else {
            Validations.validateRange(
                "Buffer pointer position or null count",
                positionOrNullCount,
                Validations.PROTOCOL_BYTE_MIN,
                Validations.PROTOCOL_BYTE_MAX
            );
        }
    }

    public static SetBufferPointerCommand reset() {
        return new SetBufferPointerCommand(Operation.RESET, RESET_VALUE);
    }

    @Override
    public byte[] getPayloadBytes() {
        if (operation == Operation.RESET) {
            return new byte[] { 0x16, 0x00 };
        }
        return new byte[] { 0x16, (byte) operation.ordinal(), (byte) positionOrNullCount };
    }
}
