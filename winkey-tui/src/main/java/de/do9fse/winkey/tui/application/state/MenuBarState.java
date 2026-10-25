package de.do9fse.winkey.tui.application.state;

import java.util.ArrayList;
import java.util.List;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.application.view.Menu;
import de.do9fse.winkey.tui.application.view.MenuItem;
import dev.tamboui.widgets.list.ListState;

public class MenuBarState {
    private final List<Menu> menus;

    private ListState listState;
    private Menu selectedMenu = null;

    public MenuBarState() {
        final MenuItem[] itemType = new MenuItem[0];

        this.menus = new ArrayList<>();
        this.menus.addAll(
            List.of(
                new Menu(
                    "File", 'f',
                    List.of(
                        new MenuItem("Exit", Action.FILE_EXIT)
                    ).toArray(itemType)
                ),
                new Menu(
                    "Device", 'd',
                    List.of(
                        new MenuItem("Configure"),
                        new MenuItem("Open"),
                        new MenuItem("Close")
                    ).toArray(itemType)
                )
            )
        );
    }
  
    public List<Menu> getMenus() {
        return menus;
    }

    public Menu getSelectedMenu() {
        return selectedMenu;
    }

    public ListState getListState() {
        return listState;
    }
    
    public void openMenu(final int menuIndex) {
        closeMenu();
        this.selectedMenu = menus.get(menuIndex);
        this.listState = new ListState();
    }

    public void closeMenu() {
        this.selectedMenu = null;
        this.listState = null;
    }

    public boolean isMenuOpen() {
        return selectedMenu != null;
    }

    public void nextItem() {
        if (!isMenuOpen()) {
            return;
        }

        this.listState.selectNext(selectedMenu.items().length);
    }

    public void previousItem() {
        if (!isMenuOpen()) {
            return;
        }

        this.listState.selectPrevious();
    }
}
