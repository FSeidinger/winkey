package de.do9fse.winkey.tui.application;

import java.util.Objects;

import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.widget.StatefulWidget;
import dev.tamboui.widget.Widget;

public class WidgetAdapter<S> implements Widget {
    private final S state;
    private final StatefulWidget<S> widget;

    public WidgetAdapter(final S state, final StatefulWidget<S> widget) {
        this.state = Objects.requireNonNull(state, "State must not be null");
        this.widget = Objects.requireNonNull(widget, "Widget must not be null");
    }

    @Override
    public void render(final Rect area, final Buffer buffer) {
        widget.render(area, buffer, state);
    }
}
