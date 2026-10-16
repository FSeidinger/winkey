package de.do9fse.winkey.terminal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.lib.core.model.ApplicationState;
import de.do9fse.winkey.lib.core.model.DeviceVersion;
import de.do9fse.winkey.lib.core.model.commands.admin.HostOpenCommand;
import de.do9fse.winkey.lib.core.model.responses.VersionResponse;
import de.do9fse.winkey.lib.core.port.out.Transport;
import de.do9fse.winkey.terminal.test.tools.AbstractCommandTest;
import de.do9fse.winkey.terminal.test.tools.ArgumentsBuilder;

public class HostOpenCommandTest extends AbstractCommandTest {
    private EnterHostModeCommand command;

    @BeforeEach
    void setUp() {
        command = new EnterHostModeCommand();
    }

    @Test
    void rejectsWhenDeviceIsClosed() {
        assertThatThrownBy(() -> command.execute(session, ArgumentsBuilder.EMPTY))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Device is closed. Please call 'connect' first.");
    }

    @Test
    void rejectsWhenDeviceIsAlreadyInHostMode() {
        context.transitionToState(ApplicationState.INITIALIZING);
        context.transitionToState(ApplicationState.STANDALONE_MODE);
        context.transitionToState(ApplicationState.HOST_MODE);

        assertThatThrownBy(() -> command.execute(session, ArgumentsBuilder.EMPTY))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Device is already in host mode.");
    }

    @Test
    void entersHostModeSuccessfully() throws Exception {
        final Transport transport = mock(Transport.class);
        when(transport.submitJob(any(HostOpenCommand.class), any())).thenAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            final CompletableFuture<VersionResponse> future = invocation.getArgument(1);
            final VersionResponse versionResponse = new VersionResponse(new DeviceVersion(2, 3));
            context.setDeviceVersion(versionResponse.version());
            context.transitionToState(ApplicationState.HOST_MODE);
            future.complete(versionResponse);
            return null;
        });

        context.setTransport(transport);
        context.transitionToState(ApplicationState.INITIALIZING);
        context.transitionToState(ApplicationState.STANDALONE_MODE);

        command.execute(session, ArgumentsBuilder.EMPTY);

        assertEquals(ApplicationState.HOST_MODE, context.getState());
        assertEquals(de.do9fse.winkey.lib.core.model.ProtocolVersion.V2, context.getProtocolVersion());
        assertEquals(new DeviceVersion(2, 3), context.getDeviceVersion());
    }
}

