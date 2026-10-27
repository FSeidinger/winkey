package de.do9fse.winkey.tui.application.state;

import java.util.List;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.model.Menu;
import de.do9fse.winkey.tui.model.MenuItem;
import dev.tamboui.tui.bindings.ActionHandler;

public class AppState {
    private final ActionHandler actionHandler;

    private final DialogState dialogState;

    private final MenuBarState menuBarState;
    private final MenuItemState menuItemState;

    private final StatusBarState statusBarState;

    public AppState(final ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
        this.dialogState = new DialogState();
        final List<Menu> menus = createMenus();
        this.menuItemState = new MenuItemState(menus);
        this.menuBarState = new MenuBarState(menus, menuItemState);
        this.statusBarState = new StatusBarState();
    }

    public ActionHandler getActionHandler() {
        return actionHandler;
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
