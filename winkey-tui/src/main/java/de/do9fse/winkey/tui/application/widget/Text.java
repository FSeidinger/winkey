package de.do9fse.winkey.tui.application.widget;

import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Alignment;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Style;
import dev.tamboui.widget.Widget;

public class Text implements Widget {
    private final String text;
    private final Alignment alignment;

    public Text(final String text) {
        this(text, Alignment.LEFT);
    }

    public Text(final String text, final Alignment alignment) {
        this.text = text;
        this.alignment = alignment;
    }

    @Override
    public void render(final Rect area, final Buffer buffer) {
        final int textWidth = text.length();

        final int posX = switch (alignment) {
            case RIGHT -> area.right() - textWidth;
            case CENTER -> area.left() + ((area.width() - textWidth) / 2);
            default -> area.left();
        };

        buffer.setString(posX, area.y(), text, Style.EMPTY);
    }
}
