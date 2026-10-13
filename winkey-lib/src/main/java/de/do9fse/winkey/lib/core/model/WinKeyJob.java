package de.do9fse.winkey.lib.core.model;

import java.util.concurrent.CompletableFuture;

import de.do9fse.winkey.lib.core.model.commands.WinKeyImmediateCommand;
import de.do9fse.winkey.lib.core.model.responses.WinKeyResponse;

public record WinKeyJob(WinKeyImmediateCommand command, CompletableFuture<WinKeyResponse> response) {}
