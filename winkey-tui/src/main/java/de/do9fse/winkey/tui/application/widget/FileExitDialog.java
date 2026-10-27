package de.do9fse.winkey.tui.application.widget;

import java.util.List;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.application.state.AppState;
import de.do9fse.winkey.tui.application.state.DialogState;
import dev.tamboui.layout.Size;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;

public class FileExitDialog extends Dialog {
    public FileExitDialog(final AppState state) {
        super(
            state,
            "Exit application",
            "You really want to exit the application?",
            new Size(20, 8)
        );
    }

    @Override
    public List<Button> getButtons() {
        return List.of(
            Buttons.OK,
            Buttons.ABORT
        );
    }

    @Override
    public EventResult handleEvent(KeyEvent event) {
        final DialogState state = this.state.getDialogState();

        if (state.isOpen()) {
            return switch (event) {
                case final KeyEvent e when e.isCancel() -> {
                    state.close();;
                    yield EventResult.HANDLED;
                }

                case final KeyEvent e when e.isConfirm() -> {
                    state.close();
                    this.state.getActionHandler().fire(Action.EXIT.id());
                    yield EventResult.HANDLED;
                }

                default -> EventResult.UNHANDLED;
            };
        }

        return EventResult.UNHANDLED;
    }
   
}
