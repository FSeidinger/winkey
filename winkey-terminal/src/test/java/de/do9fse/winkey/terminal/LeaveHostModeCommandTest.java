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
import de.do9fse.winkey.lib.core.model.commands.admin.HostCloseCommand;
import de.do9fse.winkey.lib.core.model.responses.EmptyResponse;
import de.do9fse.winkey.lib.core.port.out.Transport;
import de.do9fse.winkey.terminal.test.tools.AbstractCommandTest;
import de.do9fse.winkey.terminal.test.tools.ArgumentsBuilder;

public class LeaveHostModeCommandTest extends AbstractCommandTest {
    private LeaveHostModeCommand command;

    @BeforeEach
    void setUp() {
        command = new LeaveHostModeCommand();
    }

    @Test
    void rejectsWhenDeviceIsClosed() {
        assertThatThrownBy(() -> command.execute(session, ArgumentsBuilder.EMPTY))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Device is closed. Please call 'connect' first.");
    }

    @Test
    void rejectsWhenDeviceIsNotInHostMode() {
        context.transitionToState(ApplicationState.INITIALIZING);
        context.transitionToState(ApplicationState.STANDALONE_MODE);

        assertThatThrownBy(() -> command.execute(session, ArgumentsBuilder.EMPTY))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Device is not in host mode.");
    }

    @Test
    void leavesHostModeSuccessfully() throws Exception {
        final Transport transport = mock(Transport.class);
        when(transport.submitJob(any(HostCloseCommand.class), any())).thenAnswer(invocation -> {
            final CompletableFuture<EmptyResponse> future = invocation.getArgument(1);
            future.complete(new EmptyResponse());
            context.transitionToState(ApplicationState.STANDALONE_MODE);
            return null;
        });

        context.setTransport(transport);
        context.transitionToState(ApplicationState.INITIALIZING);
        context.transitionToState(ApplicationState.STANDALONE_MODE);
        context.transitionToState(ApplicationState.HOST_MODE);

        command.execute(session, ArgumentsBuilder.EMPTY);

        assertEquals(ApplicationState.STANDALONE_MODE, context.getState());
    }
}
