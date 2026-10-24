package de.do9fse.winkey.tui.application;

import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.layout.dock.Dock;
import dev.tamboui.widget.StatefulWidget;
import dev.tamboui.widget.Widget;

public class AppView implements StatefulWidget<AppState> {
    private final MenuBarState menuBarState;
    private final StatusBarState statusBarState;

    private final MenuBarView menuBarView;
    private final StatusBarView statusBarView;

    public AppView() {
        this.menuBarState = new MenuBarState();
        this.statusBarState = new StatusBarState();

        this.menuBarView = new MenuBarView();
        this.statusBarView = new StatusBarView();
    }

    @Override
    public void render(final Rect area, final Buffer buffer, final AppState state) {
        final Widget menuBar = new WidgetAdapter<>(menuBarState, menuBarView);
        final Widget statusBar = new WidgetAdapter<>(statusBarState, statusBarView);
        
        final Widget page = Dock
            .builder()
            .top(menuBar)
            .bottom(statusBar)
            .build();

        page.render(area, buffer);
    }
}
