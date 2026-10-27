package de.do9fse.winkey.tui.application.view;

import static dev.tamboui.toolkit.Toolkit.column;
import static dev.tamboui.toolkit.Toolkit.dialog;
import static dev.tamboui.toolkit.Toolkit.panel;
import static dev.tamboui.toolkit.Toolkit.row;
import static dev.tamboui.toolkit.Toolkit.spacer;
import static dev.tamboui.toolkit.Toolkit.text;

import dev.tamboui.toolkit.element.Element;

public class Dialogs {
    public static Element fileExitDialog() {
        return dialog(
            spacer(),
            column(
                text("You really want to exit").centered(),
                text("the application?").centered()
            ),
            spacer(),
            row(
                panel(row(spacer(), text("Y").underlined(), text("es"), spacer())).fill(),
                panel(row(spacer(), text("N").underlined(), text("o"), spacer())).fill()
            )
        ).title("Exit application")
        .minWidth(30);
    }
}
