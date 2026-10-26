package de.do9fse.winkey.tui.application.state;

import java.util.Objects;

import dev.tamboui.toolkit.elements.DialogElement;

public class DialogState {
    private DialogElement dialog = null;


    public DialogElement getDialogElement() {
        return dialog;
    }

    public boolean isOpen() {
        return dialog != null;
    }

    public void open(final DialogElement dialog) {
        this.dialog = Objects.requireNonNull(dialog, "Dialog must not be null");
    }

    public void close() {
        this.dialog = null;
    }
}
