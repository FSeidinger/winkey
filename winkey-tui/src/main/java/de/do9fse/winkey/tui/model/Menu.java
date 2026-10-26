package de.do9fse.winkey.tui.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.do9fse.winkey.tui.application.action.Action;
import dev.tamboui.style.Style;
import dev.tamboui.text.Span;

public record Menu(String title, Character shortcut, Action action, List<MenuItem> items) {
    public static final Character NO_SHORTCUT = Character.valueOf(Character.MIN_VALUE);

    public Menu(final String title) {
        this(title, null, null, Collections.emptyList());
    }

    public Menu(final String title, final List<MenuItem> items) {
        this(title, null, null, items);
    }

    public Menu(final String title, final Character shortcut, final List<MenuItem> items) {
        this(title, shortcut, null, items);
    }

    public Menu(final String title, final Character shortcut, final Action action, final List<MenuItem> items) {
        this.title = Objects.requireNonNull(title, "Title must not be null");
        this.shortcut = Objects.requireNonNullElse(shortcut, NO_SHORTCUT);
        this.action = Objects.requireNonNullElse(action, Action.NO_ACTION);
        this.items = Objects.requireNonNull(items, "Items must not be null");
    }

    public static List<Span> convertTitle(final String title, final Character shortcut) {
        if (title == null || title.isBlank()) {
            return List.of();
        }

        if (shortcut == null || shortcut.equals(NO_SHORTCUT)) {
            return List.of(Span.raw(title));
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
