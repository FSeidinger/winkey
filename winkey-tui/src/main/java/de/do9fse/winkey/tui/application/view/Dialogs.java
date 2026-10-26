package de.do9fse.winkey.tui.application.view;

import dev.tamboui.toolkit.Toolkit;
import dev.tamboui.toolkit.elements.DialogElement;

public class Dialogs {
    public static DialogElement fileExitDialog() {
        return Toolkit
            .dialog("Exit?");
    }
}
