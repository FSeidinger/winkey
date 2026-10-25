package de.do9fse.winkey.tui.application.view;

import java.util.Objects;

import de.do9fse.winkey.tui.application.state.AppState;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.layout.dock.Dock;
import dev.tamboui.widget.StatefulWidget;
import dev.tamboui.widget.Widget;

public class AppView implements StatefulWidget<AppState> {
    private final AppState appState;

    private final MenuBarView menuBarView;
    private final StatusBarView statusBarView;

    public AppView(final AppState appState) {
        this.appState = Objects.requireNonNull(appState, "App state must not be null");
        this.menuBarView = new MenuBarView();
        this.statusBarView = new StatusBarView();
    }

    @Override
    public void render(final Rect area, final Buffer buffer, final AppState state) {
        final Widget menuBar = new WidgetAdapter<>(appState.getMenuBarState(), menuBarView);
        final Widget statusBar = new WidgetAdapter<>(appState.getStatusBarState(), statusBarView);
        
        final Widget page = Dock
            .builder()
            .top(menuBar)
            .bottom(statusBar)
            .build();

        page.render(area, buffer);
    }
}
