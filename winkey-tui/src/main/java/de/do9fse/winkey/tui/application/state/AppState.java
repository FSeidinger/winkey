package de.do9fse.winkey.tui.application.state;

import java.util.List;
import java.util.Objects;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.model.Menu;
import de.do9fse.winkey.tui.model.MenuItem;

public class AppState {
    private final MenuBarState menuBarState;
    private final StatusBarState statusBarState;
    private final DialogState dialogState;

    public AppState() {
        this(
            populateMenuBar(),
            new StatusBarState(),
            new DialogState()
        );
    }

    public AppState(final MenuBarState menuBarState, final StatusBarState statusBarState, final DialogState dialogState) {
        this.menuBarState = Objects.requireNonNull(menuBarState, "Menu bar state must not be null");
        this.statusBarState = Objects.requireNonNull(statusBarState, "Status bar state must not be null");
        this.dialogState = Objects.requireNonNull(dialogState, "Dialog state must not be null");
    }

    public MenuBarState getMenuBarState() {
        return menuBarState;
    }

    public StatusBarState getStatusBarState() {
        return statusBarState;
    }

    public DialogState getDialogState() {
        return dialogState;
    }

    private static MenuBarState populateMenuBar() {
        return new MenuBarState(
            List.of(
                new Menu(
                    "File", 'f', List.of(
                        new MenuItem("Exit", Action.FILE_EXIT)
                    )
                ),
                new Menu(
                    "Device", 'd', List.of(
                        new MenuItem("Configure"),
                        new MenuItem("Open"),
                        new MenuItem("Close")
                    )
                )
            )
        );
    }
}
