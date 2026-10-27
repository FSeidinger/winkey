package de.do9fse.winkey.tui.application.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.do9fse.winkey.tui.application.state.AppState;
import de.do9fse.winkey.tui.application.state.MenuItemState;
import de.do9fse.winkey.tui.model.Menu;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widget.Widget;

public class MenuBarView implements Widget {
    private final AppState state;

    public MenuBarView(final AppState state) {
        this.state = Objects.requireNonNull(state, "State must not be null");
    }

    @Override
    public void render(final Rect area, final Buffer buffer) {
        final List<Menu> menus = state.getMenuBarState().getMenus();

        if (area.isEmpty() || menus.isEmpty()) {
            return;
        }

        final int posX = area.x();
        final int posY = area.y();

        final List<Span> menuSpans = new ArrayList<>();
        for (final Menu menu: menus) {
            menuSpans.addAll(Menu.convertTitle(menu.title(), menu.shortcut()));
        }

        final Line menuLine = Line.from(
            menuSpans.toArray(new Span[0])
        );
        
        buffer.setLine(posX, posY, menuLine);
    }

    public EventResult handleEvent(final KeyEvent event) {
        final MenuItemState menuItemState = state.getMenuItemState();
        if (menuItemState.isClosed()) {
            for (final Menu menu: state.getMenuBarState().getMenus()) {
                if (event.hasAlt() && event.isChar(menu.shortcut())) {
                    menuItemState.open(menu);
                    return EventResult.HANDLED;
                }
            }
        }

        return EventResult.UNHANDLED;
    }
}
