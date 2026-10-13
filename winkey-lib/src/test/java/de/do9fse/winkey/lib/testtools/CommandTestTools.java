package de.do9fse.winkey.lib.testtools;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import de.do9fse.winkey.lib.core.model.commands.Command;

public final class CommandTestTools {
    private CommandTestTools() {}

    public static void assertPayload(final Command command, final byte[] expectedBytes) {
        assertArrayEquals(expectedBytes, command.toData());
    }
}
