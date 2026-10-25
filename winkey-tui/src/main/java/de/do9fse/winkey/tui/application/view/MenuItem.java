package de.do9fse.winkey.tui.application.view;

import java.util.Objects;

import de.do9fse.winkey.tui.application.action.Action;

public record MenuItem(String title, Action action) {
    public MenuItem(final String title) {
        this(title, null);
    }

    public MenuItem(final String title, final Action action) {
        this.title = Objects.requireNonNull(title, "Title must not be null");
        this.action = Objects.requireNonNullElse(action, Action.NO_ACTION);
    }
}
