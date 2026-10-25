package de.do9fse.winkey.tui.application.view;

import de.do9fse.winkey.tui.application.state.StatusBarState;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.Line;
import dev.tamboui.widget.StatefulWidget;

public class StatusBarView implements StatefulWidget<StatusBarState> {
    @Override
    public void render(final Rect area, final Buffer buffer, final StatusBarState state) {
        buffer.setStyle(area, Style.EMPTY.bg(Color.BLUE).fg(Color.WHITE));

        int posX = area.x();
        int posY = area.y();

        final Line menuLine = Line.from(
            "Status: xxx"
        );
        
        buffer.setLine(posX, posY, menuLine);
    }
}
