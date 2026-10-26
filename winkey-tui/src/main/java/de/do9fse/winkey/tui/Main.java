package de.do9fse.winkey.tui;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.application.state.AppState;
import de.do9fse.winkey.tui.application.state.MenuBarState;
import de.do9fse.winkey.tui.application.state.MenuItemState;
import de.do9fse.winkey.tui.application.view.AppView;
import de.do9fse.winkey.tui.application.view.Dialogs;
import de.do9fse.winkey.tui.model.Menu;
import de.do9fse.winkey.tui.model.MenuItem;
import de.do9fse.winkey.tui.model.UseCaseService;
import dev.tamboui.terminal.Frame;
import dev.tamboui.toolkit.element.DefaultRenderContext;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.element.RenderContext;
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

    private final AppState state = new AppState();
    private final AppView view = new AppView(state);

    private final RenderContext context = DefaultRenderContext.createEmpty();

    private final ActionHandler actionHandler = new ActionHandler(BindingSets.standard())
        .on(Action.FILE_EXIT.id(), (event, action) -> {
            LOGGER.info("Handling action '{}'", action);
            this.useCases.onQuit();
        });

    private void run() throws Exception {
        try (var appRunner = TuiRunner.create()) {
            this.useCases = new UseCaseService(appRunner);
            appRunner.run(this::eventHandler, this::renderer);
        }
    }

    private boolean eventHandler(final Event event, final TuiRunner runner) {
        if (event instanceof final KeyEvent e) {
            return keyEventHandler(e, state, runner);
        }
       
        return false;
    }

    private void renderer(final Frame frame) {
        frame.renderStatefulWidget(view, frame.area(), state);

        final Element fileExitElement = Dialogs.fileExitDialog();
        fileExitElement.render(frame, frame.area(), context);
    }

    private boolean keyEventHandler(final KeyEvent event, final AppState state, final TuiRunner runner) {
        if (handledGlobalEvent(event, state)) {
            return true;
        }
        
        if (handledMenuBarEvent(event, state)) {
            return true;
        }

        if (handledMenuEvent(event, state)) {
            return true;
        }

        return false;
    }

    private boolean handledGlobalEvent(final KeyEvent event, final AppState state) {
        if (event.isQuit()) {
            actionHandler.fire(Action.FILE_EXIT.id());
            return true;
        }

        return false;
    }

    private boolean handledMenuBarEvent(final KeyEvent event, final AppState state) {
        final MenuBarState menuBarState = state.getMenuBarState();
        final List<Menu> menus = menuBarState.getMenus();
        for (int index = 0; index < menus.size(); index++) {
            final Menu menu = menus.get(index);

            if (event.hasAlt() && event.isChar(menu.shortcut())) {
                menuBarState.open(index);
                return true;
            }
        }

        return false;
    }

    private boolean handledMenuEvent(final KeyEvent event, final AppState state) {
        final MenuItemState menuItemState = state.getMenuBarState().getMenuItemState();
        if (menuItemState.isOpen()) {

            return switch (event) {
                case final KeyEvent e when e.isDown() -> {
                    menuItemState.next();
                    yield true;
                }

                case final KeyEvent e when e.isUp() -> {
                    menuItemState.previous();
                    yield true;
                }

                case final KeyEvent e when e.isSelect() -> {
                    final int selectedItemIndex = menuItemState.getListState().selected();
                    final MenuItem selectedItem = menuItemState.getItems().get(selectedItemIndex);
                    final Action action = selectedItem.action();

                    if (action != Action.NO_ACTION) {
                        actionHandler.fire(action.id());
                    }

                    yield true;
                }

                case final KeyEvent e when e.isCancel() -> {
                    menuItemState.close();
                    yield true;
                }

                default -> false;
            };
        }

        return false;
    }
}
