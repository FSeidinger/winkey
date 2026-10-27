package de.do9fse.winkey.tui.application.view;

import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.Line;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widget.Widget;

public class StatusBarView implements Widget {
    @Override
    public void render(final Rect area, final Buffer buffer) {
        buffer.setStyle(area, Style.EMPTY.bg(Color.BLUE).fg(Color.WHITE));

        int posX = area.x();
        int posY = area.y();

        final Line menuLine = Line.from(
            "Status: xxx"
        );
        
        buffer.setLine(posX, posY, menuLine);
    }

    public EventResult handleEvent(final KeyEvent event) {
        return EventResult.UNHANDLED;
    }
}
