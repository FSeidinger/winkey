package de.do9fse.winkey.tui.application.state;

import java.util.Objects;

import dev.tamboui.toolkit.elements.DialogElement;

public class DialogState {
    private DialogElement activeDialog = null;

    public boolean isOpen() {
        return activeDialog != null;
    }

    public void open(final DialogElement dialog) {
        this.activeDialog = Objects.requireNonNull(dialog, "Dialog must not be null");
    }

    public void close() {
        this.activeDialog = null;
    }
}
