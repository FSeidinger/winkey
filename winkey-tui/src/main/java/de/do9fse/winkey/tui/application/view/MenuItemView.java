package de.do9fse.winkey.tui.application.view;

import java.util.List;
import java.util.Objects;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.application.state.AppState;
import de.do9fse.winkey.tui.application.state.MenuItemState;
import de.do9fse.winkey.tui.model.Menu;
import de.do9fse.winkey.tui.model.MenuItem;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Position;
import dev.tamboui.layout.Rect;
import dev.tamboui.layout.Size;
import dev.tamboui.style.Style;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widget.Widget;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.block.BorderType;
import dev.tamboui.widgets.block.Borders;
import dev.tamboui.widgets.list.ListWidget;

public class MenuItemView implements Widget {
    private final AppState state;

    public MenuItemView(final AppState state) {
        this.state = Objects.requireNonNull(state, "State must not be null");
    }

    @Override
    public void render(final Rect area, final Buffer buffer) {
        final MenuItemState state = this.state.getMenuItemState();
        final String[] items = state
            .getItems()
            .stream()
            .map((item) -> item.title())
            .toList()
            .toArray(new String[0]);

        final ListWidget dropDown = ListWidget
            .builder()
            .items(items)
            .block(
                Block
                    .builder()
                    .borders(Borders.ALL)
                    .borderType(BorderType.DOUBLE)
                    .build()
            )
            .highlightStyle(Style.EMPTY.bold())
            .build();


        dropDown.render(
            calculatePopupArea(
                state.getMenus(),
                state.getMenu()
            ),
            buffer,
            state.getListState()
        );
    }

    public EventResult handleEvent(final KeyEvent event) {
        final MenuItemState state = this.state.getMenuItemState();
        if (state.isOpen()) {
            return switch (event) {
                case final KeyEvent e when e.isUp() -> {
                    state.previous();
                    yield EventResult.HANDLED;
                }

                case final KeyEvent e when e.isDown() -> {
                    state.next();
                    yield EventResult.HANDLED;
                }

                case final KeyEvent e when e.isCancel() -> {
                    state.close();
                    yield EventResult.HANDLED;
                }

                case final KeyEvent e when e.isSelect() -> {
                    // get selected item if any
                    final Integer selectedItemIndex = state.getListState().selected();
                    if (selectedItemIndex == null) {
                        yield EventResult.UNHANDLED;
                    }

                    // resolve action while popup is still open
                    final MenuItem selectedMenuItem = state.getItems().get(selectedItemIndex);
                    final Action action = selectedMenuItem.action();

                    // now close popup -> destroys context
                    state.close();

                    // notify application
                    this.state.getActionHandler().fire(action.id());
                    yield EventResult.HANDLED;
                }

                default -> EventResult.UNHANDLED;
            };
        }

        return EventResult.UNHANDLED;
    }

    private Rect calculatePopupArea(final List<Menu> menus, final Menu selectedMenu) {
        // start with origin (0,0)
        Position position = Position.ORIGIN;

        // translate on y axis -> one line below the menu bar
        position = position.offset(1, 1);

        for (final Menu menu: menus) {
            if (menu.equals(selectedMenu)) {
                break;
            }

            // translate on x axis -> align to next menu
            position = position.offset(menu.title().length() + 2, 0);
        }

        
        final Size size = calculatePopupSize(selectedMenu);
        return Rect.of(position, size);
    }

    private Size calculatePopupSize(final Menu selectedMenu) {
        int width = 0;

        for (final MenuItem menuItem: selectedMenu.items()) {
            final int itemSize = menuItem.title().length();

            // adjust to new width if item is wider than rolling width
            width = Math.max(width, itemSize + 6);
        }

        // set fixed height based on count of items plus borders
        final int height = selectedMenu.items().size() + 2;

        return new Size(width, height);
    }
}
