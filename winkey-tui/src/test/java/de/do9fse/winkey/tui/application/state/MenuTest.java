package de.do9fse.winkey.tui.application.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.google.gson.Gson;

import de.do9fse.winkey.tui.model.Menu;
import dev.tamboui.style.Style;
import dev.tamboui.text.Span;

public class MenuTest {
    @CsvSource(
        nullValues = "null",
        useHeadersInDisplayName = true,
        textBlock = """
        Title
        null
        ''
        ' '
        """
    )
    @ParameterizedTest
    void assertThatMissingTitleCanBeConverted(final String title) {
        final List<Span> spans = Menu.convertTitle(title, null);
        assertThat(spans.size()).isEqualTo(0);
    }

    @CsvSource(
        useHeadersInDisplayName = true,
        textBlock = """
        Title,  Shortcut
        Heinz,  
        Heinz,  0
        Heinz,  f
        """
    )
    @ParameterizedTest
    void assertThatMissingShortcutCanBeConverted(final String title, final String shortcutString) {
        final Character shortcut = fromString(shortcutString);
        final List<Span> spans = Menu.convertTitle(title, shortcut);
        assertThat(spans)
            .hasSize(1)
            .containsExactly(Span.raw(title));
    }

    @CsvSource(
        useHeadersInDisplayName = true,
        textBlock = """
        Title,  Shortcut,   Spans
        F,      f,          '[{ "text": " ", "style": "none" }, { "text": "F", "style": "underlined" }, { "text": " ", "style": "none" }]'
        Fi,     f,          '[{ "text": " ", "style": "none" }, { "text": "F", "style": "underlined" }, { "text": "i ", "style": "none" }]'
        Fi,     i,          '[{ "text": " F", "style": "none" }, { "text": "i", "style": "underlined" }, { "text": " ", "style": "none" }]'
        File,   f,          '[{ "text": " ", "style": "none" }, { "text": "F", "style": "underlined" }, { "text": "ile ",  "style": "none" }]'
        File,   i,          '[{ "text": " F", "style": "none" }, { "text": "i", "style": "underlined"  }, { "text": "le ", "style": "none" }]'
        File,   l,          '[{ "text": " Fi","style": "none" }, { "text": "l", "style": "underlined"  }, { "text": "e ", "style": "none"  }]'
        """
    )
    @ParameterizedTest
    void assertThatTitleCanBeConverted(final String title, final String shortcutString, final String spansString) {
        final Gson gson = new Gson();
        final List<Span> expected = Stream.of(gson
            .fromJson(spansString, JsonSpan[].class))
            .map((jsonSpan) -> {
                return switch (jsonSpan.style()) {
                    case "underlined" -> Span.styled(jsonSpan.text(), Style.EMPTY.underlined());
                    default -> Span.raw(jsonSpan.text());
                };
            })
            .toList();
        
        final List<Span> spans = Menu.convertTitle(title, fromString(shortcutString));
        assertThat(spans).isEqualTo(expected);
    }

    private final Character fromString(final String characterString) {
        return switch(characterString) {
            case null -> null;
            case "0" -> Menu.NO_SHORTCUT;
            default -> Character.valueOf(characterString.charAt(0));
        };
    }

    private record JsonSpan(String text, String style) {}
}
