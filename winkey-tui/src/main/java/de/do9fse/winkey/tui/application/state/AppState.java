package de.do9fse.winkey.tui.application.state;

import java.util.List;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.model.Menu;
import de.do9fse.winkey.tui.model.MenuItem;

public class AppState {
    private final MenuBarState menuBarState;
    private final MenuItemState menuItemState;

    private final StatusBarState statusBarState;
    private final DialogState dialogState;

    public AppState() {
        final List<Menu> menus = createMenus();
        this.menuItemState = new MenuItemState(menus);
        this.menuBarState = new MenuBarState(menus, menuItemState);
        this.statusBarState = new StatusBarState();
        this.dialogState = new DialogState();
    }

    public MenuBarState getMenuBarState() {
        return menuBarState;
    }

    public MenuItemState getMenuItemState() {
        return menuItemState;
    }

    public StatusBarState getStatusBarState() {
        return statusBarState;
    }

    public DialogState getDialogState() {
        return dialogState;
    }

    private static List<Menu> createMenus() {
        return List.of(
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
        );
    }
}
