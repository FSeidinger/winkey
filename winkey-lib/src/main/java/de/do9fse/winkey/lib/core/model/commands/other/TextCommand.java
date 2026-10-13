package de.do9fse.winkey.lib.core.model.commands.other;

import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration;
import de.do9fse.winkey.lib.core.model.commands.WinKeyCommand;

@ImmediateCommandConfiguration(
    allowedProtocols = { WinKeyProtocolVersion.V1, WinKeyProtocolVersion.V2, WinKeyProtocolVersion.V3 }
)
public record TextCommand(String text) implements WinKeyCommand {
    public TextCommand {
        text = Validations.validateNotBlank("Text", text);
    }

    @Override
    public byte[] getPayloadBytes() {
        return text.getBytes();
    }
}
