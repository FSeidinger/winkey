package de.do9fse.winkey.tui.application;

import java.util.Objects;

public record Menu(String title, String[] items) {
    public Menu(final String title, final String[] items) {
        this.title = Objects.requireNonNull(title, "Title must not be null");
        this.items = Objects.requireNonNull(items, "Items must not be null");
    }
}
