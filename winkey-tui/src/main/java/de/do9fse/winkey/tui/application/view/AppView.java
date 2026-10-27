package de.do9fse.winkey.tui.application.view;

import java.util.Objects;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.application.state.AppState;
import de.do9fse.winkey.tui.application.state.DialogState;
import de.do9fse.winkey.tui.application.widget.Dialog;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.layout.Rect;
import dev.tamboui.layout.dock.Dock;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widget.Widget;

public class AppView implements Widget {
    private final AppState state;

    private final MenuBarView menuBarView;
    private final MenuItemView menuItemView;
    private final StatusBarView statusBarView;

    public AppView(final AppState state) {
        this.state = Objects.requireNonNull(state, "App state must not be null");
        this.menuBarView = new MenuBarView(state);
        this.menuItemView = new MenuItemView(state);
        this.statusBarView = new StatusBarView();
    }

    @Override
    public void render(final Rect area, final Buffer buffer) {
        buffer.clear(area);
        
        final Widget page = Dock
            .builder()
            .top(menuBarView)
            .bottom(statusBarView)
            .build();

        page.render(area, buffer);
   }

    public MenuBarView getMenuBarView() {
        return menuBarView;
    }

    public MenuItemView getMenuItemView() {
        return menuItemView;
    }

    public StatusBarView getStatusBarView() {
        return statusBarView;
    }

    public EventResult handleEvent(final KeyEvent event) {
        final DialogState dialogState = state.getDialogState();
        if (dialogState.isOpen()) {
            final Dialog activeDialog = dialogState.getDialog();
            if (activeDialog.handleEvent(event) == EventResult.HANDLED) {
                return EventResult.HANDLED;
            }
        }

        final MenuItemView menuItemView = getMenuItemView();
        if (menuItemView.handleEvent(event) == EventResult.HANDLED) {
            return EventResult.HANDLED;
        }

        if (menuBarView.handleEvent(event) == EventResult.HANDLED) {
            return EventResult.HANDLED;
        }
           
        if (globalKeyEventHandler(event) == EventResult.HANDLED) {
            return EventResult.HANDLED;
        }

        return EventResult.UNHANDLED;
    }

    private EventResult globalKeyEventHandler(final KeyEvent event) {
        if (event.isQuit()) {
            state.getActionHandler().fire(Action.FILE_EXIT.id());
            return EventResult.HANDLED;
        }

        return EventResult.UNHANDLED;
    }
}
