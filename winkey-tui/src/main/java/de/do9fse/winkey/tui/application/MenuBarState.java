package de.do9fse.winkey.tui.application;

import java.util.List;

import dev.tamboui.widgets.list.ListState;

public class MenuBarState {
    private final List<Menu> menus = List.of(
        new Menu("File", new String[] { "Save", "Exit" }),
        new Menu("Device", new String[] { "Configure" , "Open", "Close" })
    );

    private ListState listState;

    private Menu selectedMenu = null;

    public List<Menu> getMenus() {
        return menus;
    }

    public Menu getSelectedMenu() {
        return selectedMenu;
    }

    public ListState getListState() {
        return listState;
    }
    
    public void enableMenu(final int menuIndex) {
        this.selectedMenu = menus.get(menuIndex);
        this.listState = new ListState();
    }

    public void disableMenu() {
        this.selectedMenu = null;
        this.listState = null;
    }
}
