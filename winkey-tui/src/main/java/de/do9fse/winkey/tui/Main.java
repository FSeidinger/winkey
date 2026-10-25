package de.do9fse.winkey.tui;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.do9fse.winkey.tui.application.action.Action;
import de.do9fse.winkey.tui.application.state.AppState;
import de.do9fse.winkey.tui.application.state.MenuBarState;
import de.do9fse.winkey.tui.application.state.StatusBarState;
import de.do9fse.winkey.tui.application.view.AppView;
import de.do9fse.winkey.tui.application.view.Menu;
import de.do9fse.winkey.tui.application.view.MenuItem;
import de.do9fse.winkey.tui.model.UseCaseService;
import dev.tamboui.terminal.Frame;
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

    private final MenuBarState menuBarState = new MenuBarState();
    private final StatusBarState statusBarState = new StatusBarState();
    private final AppState appState = new AppState(menuBarState, statusBarState);
    private final AppView view = new AppView(appState);

    private final ActionHandler actionHandler = new ActionHandler(BindingSets.standard())
        .on(Action.FILE_EXIT.toString(), event -> {
            LOGGER.info("Handling {}", event.toString());
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
            return keyEventHandler(e, runner);
        }
       
        return false;
    }

    private void renderer(final Frame frame) {
        frame.renderStatefulWidget(view, frame.area(), appState);
    }

    private boolean keyEventHandler(final KeyEvent event, final TuiRunner runner) {
        if (handledGlobalEvent(event)) {
            return true;
        }
        
        if (handledMenuBarEvent(event)) {
            return true;
        }

        if (handledMenuEvent(event)) {
            return true;
        }

        return false;
    }

    private boolean handledGlobalEvent(final KeyEvent event) {
        if (event.isQuit()) {
            actionHandler.fire(Action.FILE_EXIT.toString());
            return true;
        }

        return false;
    }

    private boolean handledMenuBarEvent(final KeyEvent event) {
        final List<Menu> menus = menuBarState.getMenus();
        for (int index = 0; index < menus.size(); index++) {
            final Menu menu = menus.get(index);

            if (event.hasAlt() && event.isChar(menu.shortcut())) {
                menuBarState.openMenu(index);
                return true;
            }
        }

        return false;
    }

    private boolean handledMenuEvent(final KeyEvent event) {
        if (menuBarState.isMenuOpen()) {
            final Menu selectedMenu = menuBarState.getSelectedMenu();

            return switch (event) {
                case final KeyEvent e when e.isUp() -> {
                    menuBarState.previousItem();
                    yield true;
                }

                case final KeyEvent e when e.isDown() -> {
                    menuBarState.nextItem();
                    yield true;
                }

                case final KeyEvent e when e.isSelect() -> {
                    final int selectedMenuItemIndex = menuBarState.getListState().selected();
                    final MenuItem selectedMenuItem = selectedMenu.items()[selectedMenuItemIndex];
                    menuBarState.closeMenu();

                    actionHandler.fire(selectedMenuItem.action().toString());
                    yield true;
                }

                case final KeyEvent e when e.isCancel() -> {
                    menuBarState.closeMenu();
                    yield true;
                }

                default -> false;
            };
        }

        return false;
    }
}
