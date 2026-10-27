package de.do9fse.winkey.tui.model;

import de.do9fse.winkey.tui.UseCases;
import de.do9fse.winkey.tui.application.state.AppState;
import de.do9fse.winkey.tui.application.widget.FileExitDialog;
import dev.tamboui.tui.TuiRunner;

public class UseCaseService implements UseCases {
    private final TuiRunner runner;
    private final AppState state;

    public UseCaseService(final TuiRunner runner, final AppState state) {
        this.runner = runner;
        this.state = state;
    }

    @Override
    public void onFileExit() {
        state.getDialogState().open(new FileExitDialog(state));
    }

    @Override
    public void onQuit() {
        runner.quit();
    }
}
