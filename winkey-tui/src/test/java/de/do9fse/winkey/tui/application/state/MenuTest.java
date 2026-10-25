package de.do9fse.winkey.tui.application.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.do9fse.winkey.tui.application.view.Menu;
import dev.tamboui.style.Style;
import dev.tamboui.text.Span;

public class MenuTest {
    @CsvSource(
        useHeadersInDisplayName = true,
        textBlock = """
        Title,  Shortcut,   Spans
        F,      'f',        3
        Fi,     'f',        3
        Fi,     'i',        3
        File,   'f',        3
        File,   'i',        3
        File,   'l',        3
        File,   'x',        1
        """
    )
    @ParameterizedTest
    void assertThatSpansCanBeCreatedFromTitle(final String title, final char shortcut, final int spanCount) {
        List<Span> spans = Menu.formatTitle(title, shortcut);

        assertThat(spans.size()).isEqualTo(spanCount);
        assertThat(spans.get(0).style()).isEqualTo(Style.EMPTY);

        if (spanCount == 3) {
            assertThat(spans.get(1).style()).isEqualTo(Style.EMPTY.underlined());
            assertThat(spans.get(2).style()).isEqualTo(Style.EMPTY);
        }
    }
}
