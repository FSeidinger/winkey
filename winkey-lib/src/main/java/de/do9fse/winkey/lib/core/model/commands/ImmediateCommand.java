package de.do9fse.winkey.lib.core.model.commands;

import java.text.MessageFormat;
import java.util.Set;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.error.WKRuntimeException;
import de.do9fse.winkey.lib.core.model.responses.CommandResponse;

public interface ImmediateCommand extends Command {
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
        final Class<ImmediateCommand> commandClass = (Class<ImmediateCommand>) getClass();
        final String commandName = commandClass.getName();

        final ImmediateCommandConfiguration configuration = commandClass.getAnnotation(ImmediateCommandConfiguration.class);
        if (configuration == null) {
            final String message = MessageFormat.format("Command {0} must support the command configuration", commandName);
            throw new WKRuntimeException(message);
        }

        final Set<ProtocolVersion> supportedKeyProtocolVersions = Set.of(configuration.allowedProtocols());
        @SuppressWarnings("unchecked")
        final Class<CommandResponse> responseClass = (Class<CommandResponse>) configuration.responseType();

        return new CommandInfo(commandClass, responseClass, supportedKeyProtocolVersions);
    }
}
