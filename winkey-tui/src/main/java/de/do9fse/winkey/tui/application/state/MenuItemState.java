package de.do9fse.winkey.tui.application.state;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.do9fse.winkey.tui.model.Menu;
import de.do9fse.winkey.tui.model.MenuItem;
import dev.tamboui.widgets.list.ListState;

public class MenuItemState {
    private final List<Menu> menus;

    private Menu menu;

    private List<MenuItem> items;
    private ListState listState;

    public MenuItemState(final List<Menu> menus) {
        this.menus = Objects.requireNonNull(menus, "Menus must not be null");
        setMembers(null);
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public Menu getMenu() {
        return menu;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public ListState getListState() {
        return listState;
    }

    public boolean isOpen() {
        return isClosed() == false;
    }

    public boolean isClosed() {
        return menu == null || items.isEmpty();
    }

    public void open(final Menu menu) {
        this.setMembers(
            Objects.requireNonNull(menu, "Menu must not be null")
        );
    }

    public void close() {
        this.setMembers(null);
    }

    public void next() {
        if (listState == null) {
            throw new IllegalStateException("Menu popup is closed");
        }

        this.listState.selectNext(menu.items().size());
    }

    public void previous() {
        if (listState == null) {
            throw new IllegalStateException("Menu popup is closed");
        }

        this.listState.selectPrevious();
    }

    private void setMembers(final Menu menu) {
        this.menu = menu;
        this.items = toMenuItems(menu);
        this.listState = toListState(menu);
    }

    private List<MenuItem> toMenuItems(final Menu menu) {
        if (menu != null) {
            return menu.items();
        } else {
            return Collections.emptyList();
        }
    }

    private ListState toListState(final Menu menu) {
        if (menu != null) {
            return new ListState();
        } else {
            return null;
        }
    }
}
