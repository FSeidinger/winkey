package de.do9fse.winkey.lib.core.model;

import java.util.concurrent.CompletableFuture;

import de.do9fse.winkey.lib.core.model.commands.ImmediateCommand;
import de.do9fse.winkey.lib.core.model.responses.Response;

public record Job(ImmediateCommand command, CompletableFuture<Response> response) {}
