package de.do9fse.winkey.terminal;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.do9fse.winkey.lib.core.model.Validations;
import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.configuration.serial.Timeout;
import de.do9fse.winkey.lib.core.model.error.WinKeyApplicationException;
import de.do9fse.winkey.lib.core.model.error.WKRuntimeException;
import de.do9fse.winkey.lib.core.model.responses.Response;
import de.do9fse.winkey.lib.core.port.out.Transport;

public interface CommandHelper {
    static Response executeCommand(final Transport transport, final ImmediateCommand command, final Timeout timeout) {
        Validations.validateNotNull("Transport", transport);
        Validations.validateNotNull("Command", command);
        Validations.validateNotNull("Timeout unit", timeout);

        try {
            final CompletableFuture<Response> response = new CompletableFuture<>();
            transport.submitJob(command, response);
            return response.get(timeout.timeout(), timeout.timeUnit());
        } catch (InterruptedException | ExecutionException | TimeoutException | WinKeyApplicationException e) {
            throw new WKRuntimeException("Failed to execute command " + command, e);
        }
    }
}
