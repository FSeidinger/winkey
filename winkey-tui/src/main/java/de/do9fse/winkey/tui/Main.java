package de.do9fse.winkey.tui;

import de.do9fse.winkey.tui.application.AppState;
import de.do9fse.winkey.tui.application.AppView;
import dev.tamboui.tui.TuiRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        try (var appRunner = TuiRunner.create()) {
            final AppState state = new AppState();
            final AppView view = new AppView();

            appRunner.run(
                (event, runner) -> {
                    return false;
                },
    
                (frame) -> {
                    frame.renderStatefulWidget(view, frame.area(), state);
                }
            );
        }
    }
}
