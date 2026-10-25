package de.do9fse.winkey.tui.application.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.do9fse.winkey.tui.application.state.MenuBarState;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Style;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.widget.StatefulWidget;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.block.BorderType;
import dev.tamboui.widgets.block.Borders;
import dev.tamboui.widgets.list.ListState;
import dev.tamboui.widgets.list.ListWidget;

public class MenuBarView implements StatefulWidget<MenuBarState> {
    @Override
    public void render(final Rect area, final Buffer buffer, final MenuBarState state) {
        final List<Menu> menus = state.getMenus();

        if (area.isEmpty() || menus.isEmpty()) {
            return;
        }

        int posX = area.x();
        int posY = area.y();

        final List<Span> menuSpans = new ArrayList<>();
        for (final Menu menu: menus) {
            menuSpans.addAll(Menu.formatTitle(menu.title(), menu.shortcut()));
        }

        final Line menuLine = Line.from(
            menuSpans.toArray(new Span[0])
        );
        
        buffer.setLine(posX, posY, menuLine);
        renderDropDown(area, buffer, state);
    }

    private void renderDropDown(final Rect area, final Buffer buffer, final MenuBarState state) {
        final Menu selectedMenu = state.getSelectedMenu();

        if (selectedMenu == null) {
            return;
        }

        final ListWidget dropDown = ListWidget
            .builder()
            .items(
                Stream
                    .of(selectedMenu.items())
                    .map((item) -> item.title())
                    .toArray(String[]::new)
            )
            .block(
                Block
                    .builder()
                    .borders(Borders.ALL)
                    .borderType(BorderType.DOUBLE)
                    .build()
            )
            .highlightStyle(Style.EMPTY.bold())
            .build();


        final Rect dropDownArea = getDropDownArea(area, state.getMenus(), selectedMenu);
        final ListState listState = state.getListState();
        dropDown.render(dropDownArea, buffer, listState);
    }

    public Rect getDropDownArea(final Rect parentArea, final List<Menu> menus, final Menu selectedMenu) {
        final int offsetX = calculateOffsetX(menus, selectedMenu);
        final int offsetY = 1;

        int width = calculateMaxItemWidth(selectedMenu);
        int height = selectedMenu.items().length + 2;

        return new Rect(parentArea.x() + offsetX, parentArea.y() + offsetY, width, height);
    }

    public int calculateMaxItemWidth(final Menu selectedMenu) {
        int maxSize = 0;

        for (final MenuItem menuItem: selectedMenu.items()) {
            final int itemSize = menuItem.title().length();
            maxSize = Math.max(maxSize, itemSize);
        }

        return maxSize + 6;
    }

    public int calculateOffsetX(final List<Menu> menus, final Menu selectedMenu) {
        int offsetX = 1;

        for (final Menu menu: menus) {
            if (menu.equals(selectedMenu)) {
                break;
            }

            offsetX += menu.title().length() + 2;
        }

        return offsetX;
    }
}
