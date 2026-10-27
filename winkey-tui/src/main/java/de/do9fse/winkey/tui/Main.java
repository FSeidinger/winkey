package de.do9fse.winkey.tui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.application.state.AppState;
import de.do9fse.winkey.tui.application.state.DialogState;
import de.do9fse.winkey.tui.application.view.AppView;
import de.do9fse.winkey.tui.application.widget.Dialog;
import de.do9fse.winkey.tui.model.UseCaseService;
import dev.tamboui.terminal.Frame;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.TuiRunner;
import dev.tamboui.tui.bindings.ActionHandler;
import dev.tamboui.tui.bindings.BindingSets;
import dev.tamboui.tui.event.Event;
import dev.tamboui.tui.event.KeyEvent;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) throws Exception {
        final Main main = new Main();
        LOGGER.info("App created");
        main.run();
        LOGGER.info("App finished");
    }

    private UseCases useCases;

    private final ActionHandler actionHandler = new ActionHandler(BindingSets.standard())
        .on(Action.FILE_EXIT.id(), (event, action) -> {
            this.useCases.onFileExit();
        })
        .on(Action.EXIT.id(), (event, action) -> {
            this.useCases.onQuit();
        });

    public final AppState state = new AppState(actionHandler);
    private final AppView view = new AppView(state);

    private void run() throws Exception {
        try (var runner = TuiRunner.create()) {
            this.useCases = new UseCaseService(runner, state);
            runner.run(this::eventHandler, this::renderer);
        }
    }

    private void renderer(final Frame frame) {
        frame.renderWidget(view, frame.area());

        final DialogState dialogState = state.getDialogState();
        if (dialogState.isOpen()) {
            final Dialog dialog = dialogState.getDialog();
            frame.renderWidget(dialog, frame.area());
            return;
        }

        if (state.getMenuItemState().isOpen()) {
            frame.renderWidget(view.getMenuItemView(), frame.area());
            return;
        }
    }

    private boolean eventHandler(final Event event, final TuiRunner runner) {
        if (event instanceof final KeyEvent e) {
            if (view.handleEvent(e) == EventResult.HANDLED) {
                return true;
            }
        }
       
        return false;
    }
}
