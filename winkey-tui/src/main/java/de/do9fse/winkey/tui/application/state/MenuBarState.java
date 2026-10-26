package de.do9fse.winkey.tui.application.state;

import java.util.List;
import java.util.Objects;

import de.do9fse.winkey.tui.model.Menu;

public class MenuBarState {
    private final List<Menu> menus;
    private MenuItemState menuItemState;

    public MenuBarState(final List<Menu> menus) {
        this.menus = Objects.requireNonNull(menus, "Menus must not be null");
        this.menuItemState = new MenuItemState();
    }
  
    public List<Menu> getMenus() {
        return menus;
    }
   
    public MenuItemState getMenuItemState() {
        return this.menuItemState;
    }

    public boolean isOpen() {
        return this.menuItemState.isOpen();
    }

    public void open(final int index) {
        final Menu menu = menus.get(index);
        this.menuItemState.open(menu);
    }

    public void close() {
        this.menuItemState.close();
    }
}
