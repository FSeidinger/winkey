package de.do9fse.winkey.lib.core.model.commands.test.suite3;

import de.do9fse.winkey.lib.core.model.commands.Command;

public class CommandWithoutAnnotation implements Command{
    @Override
    public byte[] getPayloadBytes() {
        return null;
    }
}
