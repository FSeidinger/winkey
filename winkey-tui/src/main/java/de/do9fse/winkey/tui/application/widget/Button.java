package de.do9fse.winkey.tui.application.widget;

import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Alignment;
import dev.tamboui.layout.Rect;
import dev.tamboui.layout.columns.Columns;
import dev.tamboui.widget.Widget;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.block.Borders;

public class Button implements Widget {
    private final String title;

    public Button(final String title) {
        this.title = title;
    }

    @Override
    public void render(final Rect area, final Buffer buffer) {
        final Block button = Block.builder()
            .borders(Borders.ALL)
            .build();
        button.render(area, buffer);

        final Widget buttonText = Columns
            .builder()
            .children(new Text(title, Alignment.CENTER))
            .build();
        buttonText.render(button.inner(area), buffer);
    }
}
