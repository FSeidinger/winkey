package de.do9fse.winkey.tui.application.state;

import java.util.List;
import java.util.Objects;

import de.do9fse.winkey.tui.model.Menu;

public class MenuBarState {
    private final List<Menu> menus;
    private final MenuItemState menuItemState;

    public MenuBarState(final List<Menu> menus, final MenuItemState menuItemState) {
        this.menus = Objects.requireNonNull(menus, "Menus must not be null");
        this.menuItemState = Objects.requireNonNull(menuItemState, "Menu item state must not be null");
    }
  
    public List<Menu> getMenus() {
        return menus;
    }

    public MenuItemState getMenuItemState() {
        return menuItemState;
    }
}
