package de.do9fse.winkey.lib.core.model.commands;

import java.util.Set;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.responses.CommandResponse;

public record CommandInfo(Class<ImmediateCommand> commandClass, Class<CommandResponse> responseType, Set<ProtocolVersion> allowedProtocolVersions) {
    public boolean supportsProtocol(final ProtocolVersion protocolVersion) {
        return allowedProtocolVersions.contains(protocolVersion);
    }
}
