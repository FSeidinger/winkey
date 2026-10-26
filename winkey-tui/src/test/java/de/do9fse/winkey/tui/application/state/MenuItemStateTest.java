package de.do9fse.winkey.tui.application.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.do9fse.winkey.tui.model.Menu;
import de.do9fse.winkey.tui.model.MenuItem;

public class MenuItemStateTest {
    private List<MenuItem> defaultMenuItems;
    private Menu defaultMenu;
    private List<Menu> menus;

    private MenuItemState state;

    @BeforeEach
    void setUp() {
        this.defaultMenuItems = List.of(new MenuItem("Default"));
        this.defaultMenu = new Menu("Default", defaultMenuItems);
        this.menus = List.of(defaultMenu);

        this.state = new MenuItemState(menus);
        this.state.open(defaultMenu);
    }

    @Test
    void testClose() {
        state.close();
        assertThat(state.isOpen()).isFalse();
    }

    @Test
    void testGetItems() {
        assertThat(state.getItems()).isEqualTo(defaultMenuItems);

        state.close();
        assertThat(state.getItems()).isEmpty();
    }

    @Test
    void testGetMenu() {
        assertThat(state.getMenu()).isEqualTo(defaultMenu);

        state.close();
        assertThat(state.getMenu()).isNull();
    }

    @Test
    void testIsOpen() {
        assertThat(state.isOpen()).isTrue();
    }

    @Test
    void testOpen() {
        final Menu emptyMenu = new Menu("Empty");
        state.open(emptyMenu);
        assertThat(state.isOpen()).isFalse();

        state.open(defaultMenu);
        assertThat(state.isOpen()).isTrue();
    }
}
