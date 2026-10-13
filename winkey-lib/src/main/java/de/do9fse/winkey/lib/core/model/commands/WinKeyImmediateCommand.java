package de.do9fse.winkey.lib.core.model.commands;

import java.text.MessageFormat;
import java.util.Set;

import de.do9fse.winkey.lib.core.model.WinKeyProtocolVersion;
import de.do9fse.winkey.lib.core.model.error.WinKeyRuntimeException;
import de.do9fse.winkey.lib.core.model.responses.WinKeyResponse;

public interface WinKeyImmediateCommand extends WinKeyCommand {
    /**
     * Returns the command info of this command
     *
     * <p>
     * The command info is read from the
     * {@link de.do9fse.winkey.lib.core.model.commands.ImmediateCommandConfiguration CommandConfiguration}
     * annotation. 
     * </p>
     *
     * @return The command info of this command
     */
    default CommandInfo getCommandInfo() {
        @SuppressWarnings("unchecked")
        final Class<WinKeyImmediateCommand> commandClass = (Class<WinKeyImmediateCommand>) getClass();
        final String commandName = commandClass.getName();

        final ImmediateCommandConfiguration configuration = commandClass.getAnnotation(ImmediateCommandConfiguration.class);
        if (configuration == null) {
            final String message = MessageFormat.format("Command {0} must support the command configuration", commandName);
            throw new WinKeyRuntimeException(message);
        }

        final Set<WinKeyProtocolVersion> supportedKeyProtocolVersions = Set.of(configuration.allowedProtocols());
        @SuppressWarnings("unchecked")
        final Class<WinKeyResponse> responseClass = (Class<WinKeyResponse>) configuration.responseType();

        return new CommandInfo(commandClass, responseClass, supportedKeyProtocolVersions);
    }
}
