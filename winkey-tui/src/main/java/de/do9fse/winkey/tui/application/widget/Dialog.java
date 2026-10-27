package de.do9fse.winkey.tui.application.widget;

import java.util.List;

import de.do9fse.winkey.tui.application.state.AppState;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Alignment;
import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Rect;
import dev.tamboui.layout.Size;
import dev.tamboui.layout.columns.Columns;
import dev.tamboui.layout.dock.Dock;
import dev.tamboui.style.Overflow;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widget.Widget;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.block.BorderType;
import dev.tamboui.widgets.block.Borders;
import dev.tamboui.widgets.paragraph.Paragraph;

public abstract class Dialog implements Widget {
    protected final AppState state;

    protected final String title;
    protected final String message;
    protected final Size dialogSize;

    public Dialog(final AppState state, final String title, final String message, final Size dialogSize) {
        this.state = state;
        this.title = title;
        this.message = message;
        this.dialogSize = dialogSize;
    }

    @Override
    public void render(final Rect area, final Buffer buffer) {
        final Rect dialogArea = calculateDialogArea(area, dialogSize);

        final Block dialog = Block.builder()
            .borders(Borders.ALL)
            .title(title)
            .borderType(BorderType.ROUNDED)
            .build();
        dialog.render(dialogArea, buffer);
       
        final Widget message = Paragraph
            .builder()
            .text(this.message)
            .alignment(Alignment.CENTER)
            .overflow(Overflow.WRAP_WORD)
            .build();

        final Columns buttons = Columns
            .builder()
            .columnCount(getButtons().size())
            .children(getButtons().toArray(new Button[0]))
            .build();

        final Dock dialogContent = Dock
            .builder()
            .top(message)
            .topHeight(Constraint.min(calculateMessageHeight()))
            .bottom(buttons)
            .bottomHeight(Constraint.min(3))
            .build();

        final Rect dialogContentArea = dialog.inner(dialogArea);
        dialogContent.render(dialogContentArea, buffer);
    }

    public abstract List<Button> getButtons();

    public abstract EventResult handleEvent(final KeyEvent event);

    private int calculateMessageHeight() {
        return (message.length() / dialogSize.width());
    }

    private Rect calculateDialogArea(final Rect origin, final Size dialogSize) {
        final int width = dialogSize.width();
        final int height = dialogSize.height();

        final int xCenter = origin.x() + ((origin.width() - width) / 2);
        final int yCenter = origin.y() + ((origin.height() - height) / 2);

        return new Rect(xCenter, yCenter, width, height);
    }
}
