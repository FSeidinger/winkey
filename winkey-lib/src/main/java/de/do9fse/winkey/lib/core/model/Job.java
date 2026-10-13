package de.do9fse.winkey.lib.core.model;

import java.util.concurrent.CompletableFuture;

import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.responses.CommandResponse;

public record Job(ImmediateCommand command, CompletableFuture<CommandResponse> response) {}
