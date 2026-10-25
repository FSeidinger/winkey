package de.do9fse.winkey.tui.model;

import de.do9fse.winkey.tui.UseCases;
import dev.tamboui.tui.TuiRunner;

public class UseCaseService implements UseCases {
    private final TuiRunner runner;

    public UseCaseService(final TuiRunner runner) {
        this.runner = runner;
    }

    @Override
    public void onQuit() {
        runner.quit();
    }
}
