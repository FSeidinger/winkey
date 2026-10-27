package de.do9fse.winkey.tui.application.state;

import java.util.Objects;

import de.do9fse.winkey.tui.application.widget.Dialog;

public class DialogState {
    private Dialog dialog = null;

    public Dialog getDialog() {
        return dialog;
    }

    public boolean isOpen() {
        return dialog != null;
    }

    public void open(final Dialog dialog) {
        this.dialog = Objects.requireNonNull(dialog, "Dialog must not be null");
    }

    public void close() {
        this.dialog = null;
    }
}
