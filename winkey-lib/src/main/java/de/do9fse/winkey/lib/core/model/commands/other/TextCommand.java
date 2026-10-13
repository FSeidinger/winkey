package de.do9fse.winkey.lib.core.model.commands.other;

import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.Command;

@ImmediateCommandConfiguration(
    allowedProtocols = { ProtocolVersion.V1, ProtocolVersion.V2, ProtocolVersion.V3 }
)
public record TextCommand(String text) implements Command {
    public TextCommand {
        text = Validations.validateNotBlank("Text", text);
    }

    @Override
    public byte[] getPayloadBytes() {
        return text.getBytes();
    }
}
