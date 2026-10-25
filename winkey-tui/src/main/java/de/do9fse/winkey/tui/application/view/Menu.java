package de.do9fse.winkey.tui.application.view;

import java.util.List;
import java.util.Objects;

import dev.tamboui.style.Style;
import dev.tamboui.text.Span;

public record Menu(String title, char shortcut, MenuItem[] items) {
    public Menu(final String title, final char shortcut, final MenuItem[] items) {
        this.title = Objects.requireNonNull(title, "Title must not be null");
        this.shortcut = shortcut;
        this.items = Objects.requireNonNull(items, "Items must not be null");
    }

    public static List<Span> formatTitle(final String title, final char shortcut) {
        if (title.isBlank()) {
            return List.of();
        }

        final int shortcutPos = title.toLowerCase().indexOf(shortcut);
        if (shortcutPos == -1) {
            return List.of(Span.raw(title));
        }

        final String prefix = " " + title.substring(0, shortcutPos);
        final String underlined = title.substring(shortcutPos, shortcutPos + 1);
        final String suffix = title.substring(shortcutPos + 1, title.length()) + " ";

        return List.of(
            Span.raw(prefix),
            Span.styled(underlined, Style.EMPTY.underlined()),
            Span.raw(suffix)
        );
    }
}
