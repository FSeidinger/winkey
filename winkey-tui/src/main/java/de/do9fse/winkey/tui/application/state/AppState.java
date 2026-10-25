package de.do9fse.winkey.tui.application.state;

import java.util.Objects;

public class AppState {
    private final MenuBarState menuBarState;
    private final StatusBarState statusBarState;

    public AppState(MenuBarState menuBarState, StatusBarState statusBarState) {
        this.menuBarState = Objects.requireNonNull(menuBarState, "Menu bar state must not be null");
        this.statusBarState = Objects.requireNonNull(statusBarState, "Status bar state must not be null");
    }

    public MenuBarState getMenuBarState() {
        return menuBarState;
    }

    public StatusBarState getStatusBarState() {
        return statusBarState;
    }
}
