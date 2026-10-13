package de.do9fse.winkey.lib.core.model.commands.host.immediate;

import java.text.MessageFormat;
import java.util.Arrays;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;

@ImmediateCommandConfiguration(allowedProtocols = ProtocolVersion.V2)
public record LoadDefaultsCommand(byte[] values) implements ImmediateCommand {
    private static final int DEFAULTS_BYTES = 15;

    public LoadDefaultsCommand {
        Validations.validateNotNull("Default values", values);
        
        if (values.length != DEFAULTS_BYTES) {
            final String message = MessageFormat.format(
                "Load defaults requires exactly {0} values but received {1}",
                DEFAULTS_BYTES,
                values.length
            );

            throw new IllegalArgumentException(message);
        }
        values = Arrays.copyOf(values, values.length);
    }

    @Override
    public byte[] values() {
        return Arrays.copyOf(values, values.length);
    }

    @Override
    public byte[] toData() {
        final byte[] payload = new byte[DEFAULTS_BYTES + 1];
        payload[0] = 0x0f;
        System.arraycopy(values, 0, payload, 1, DEFAULTS_BYTES);
        return payload;
    }
}
