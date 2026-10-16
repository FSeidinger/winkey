package de.do9fse.winkey.terminal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.DeviceVersion;
import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.terminal.test.tools.AbstractCommandTest;
import de.do9fse.winkey.terminal.test.tools.ArgumentsBuilder;

public class SetVersionCommandTest extends AbstractCommandTest {
    /**
     * The subject under test
     */
    private SetVersionCommand command;

    @BeforeEach
    void setUp() {
        command = new SetVersionCommand();
    }

    @Test
    void assertThatAValidVersionIsAccepted() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addArgument("V2")
            .build();

        command.execute(session, arguments);

        final ProtocolVersion expected = ProtocolVersion.V2;
        assertEquals(expected, context.getProtocolVersion());
        assertEquals(new DeviceVersion(2, 0), context.getDeviceVersion());
    }

    @Test
    void assertThatAMissingVersionFails() throws Exception {
        final String[] arguments = ArgumentsBuilder.EMPTY;

        assertThatThrownBy(() -> command.execute(session, arguments))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("The version parameter is mandatory");
    }

    @Test
    void assertThatAnInvalidVersionFails() throws Exception {
        final String[] arguments = ArgumentsBuilder
            .builder()
            .addArgument("V0")
            .build();

        final Set<ProtocolVersion> allowedValues = EnumSet.allOf(ProtocolVersion.class);
        final String expected = "Version must be one of " + allowedValues + " but was V0";
        assertThatThrownBy(() -> command.execute(session, arguments))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(expected);
    }
}
