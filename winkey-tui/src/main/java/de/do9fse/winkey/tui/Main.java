package de.do9fse.winkey.tui;

import java.util.List;

import de.do9fse.winkey.tui.application.state.AppState;
import de.do9fse.winkey.tui.application.state.MenuBarState;
import de.do9fse.winkey.tui.application.state.StatusBarState;
import de.do9fse.winkey.tui.application.view.AppView;
import de.do9fse.winkey.tui.application.view.Menu;
import de.do9fse.winkey.tui.model.UseCaseService;
import dev.tamboui.terminal.Frame;
import dev.tamboui.tui.TuiRunner;
import dev.tamboui.tui.event.Event;
import dev.tamboui.tui.event.KeyEvent;

public class Main {
    public static void main(final String[] args) throws Exception {
        final Main main = new Main();
        main.run();
    }

    private UseCases useCases;

    private final MenuBarState menuBarState = new MenuBarState();
    private final StatusBarState statusBarState = new StatusBarState();
    private final AppState appState = new AppState(menuBarState, statusBarState);
    private final AppView view = new AppView(appState);

    private void run() throws Exception {
        try (var appRunner = TuiRunner.create()) {
            this.useCases = new UseCaseService(appRunner);
            appRunner.run(this::eventHandler, this::renderer);
        }
    }

    private boolean eventHandler(final Event event, final TuiRunner runner) {
        if (event instanceof final KeyEvent keyEvent) {
            return keyEventHandler(keyEvent, runner);
        }

        return false;
    }

    private boolean keyEventHandler(final KeyEvent event, final TuiRunner runner) {
        if (event.isQuit()) {
            useCases.onQuit();
            return false;
        }

        final List<Menu> menus = menuBarState.getMenus();
        for (int index = 0; index < menus.size(); index++) {
            final Menu menu = menus.get(index);

            if (event.hasAlt() && event.isChar(menu.shortcut())) {
                menuBarState.openMenu(index);
                return true;
            }
        }

        if (menuBarState.isMenuOpen()) {
            return switch (event) {
                case KeyEvent e when e.isUp() -> {
                    menuBarState.previousItem();
                    yield true;
                }

                case KeyEvent e when e.isDown() -> {
                    menuBarState.nextItem();
                    yield true;
                }

                case KeyEvent e when e.isCancel() -> {
                    menuBarState.closeMenu();
                    yield true;
                }

                default -> false;
            };
        }

        return false;
    }

    private void renderer(final Frame frame) {
        frame.renderStatefulWidget(view, frame.area(), appState);
    }
}
