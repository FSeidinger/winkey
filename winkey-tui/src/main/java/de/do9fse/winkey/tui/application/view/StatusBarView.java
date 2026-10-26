package de.do9fse.winkey.tui.application.view;

import java.util.Objects;

import de.do9fse.winkey.tui.application.state.StatusBarState;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.Line;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widget.Widget;

public class StatusBarView implements Widget {
    private final StatusBarState state;

    public StatusBarView(StatusBarState state) {
        this.state = Objects.requireNonNull(state, "State must not be null");
    }

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
