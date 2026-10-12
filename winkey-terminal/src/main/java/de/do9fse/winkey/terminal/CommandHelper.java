package de.do9fse.winkey.terminal;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.do9fse.winkey.lib.core.model.commands.WinKeyCommand;
import de.do9fse.winkey.lib.core.model.configuration.serial.Timeout;
import de.do9fse.winkey.lib.core.model.error.WinKeyApplicationException;
import de.do9fse.winkey.lib.core.model.error.WinKeyRuntimeException;
import de.do9fse.winkey.lib.core.model.responses.WinKeyResponse;
import de.do9fse.winkey.lib.core.port.out.WinKeyTransport;

public interface CommandHelper {
    static WinKeyResponse executeCommand(final WinKeyTransport transport, final WinKeyCommand command, final Timeout timeout) {
        Objects.requireNonNull(transport, "Transport must not be null");
        Objects.requireNonNull(command, "Command must not be null");
        Objects.requireNonNull(timeout, "Timeout unit must not be null");

        try {
            final CompletableFuture<WinKeyResponse> response = new CompletableFuture<>();
            transport.submitJob(command, response);
            return response.get(timeout.timeout(), timeout.timeUnit());
        } catch (InterruptedException | ExecutionException | TimeoutException | WinKeyApplicationException e) {
            throw new WinKeyRuntimeException("Failed to execute command " + command, e);
        }
    }
}
