package de.do9fse.winkey.terminal.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LexerTest {
    private Lexer lexer = new Lexer();

    @Test
    void assertThatParsingNullLineSucceeds() throws Exception {
        final List<Token> tokens = lexer.parse(null);

        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
    }

    @Test
    void assertThatParsingEmptyLinesSucceeds() throws Exception {
        final List<Token> tokens = lexer.parse("");

        assertNotNull(tokens);
        assertTrue(tokens.isEmpty());
    }

    @Test
    void assertThatParsingEscapedAtSignSucceeds() throws Exception {
        final List<Token> tokens = lexer.parse("\\@");
        
        assertNotNull(tokens);
        assertThat(tokens)
            .contains(new Token(TokenType.TEXT, "@"));
    }

    @Test
    void assertThatParsingSimpleBackslashSucceeds() throws Exception {
        final List<Token> tokens = lexer.parse("\\");
        
        assertNotNull(tokens);
        assertThat(tokens)
            .contains(new Token(TokenType.TEXT, "\\"));
    }

    @Test
    void assertThatParsingTextSucceeds() throws Exception {
        final List<Token> tokens = lexer.parse("Howdy");
        
        final List<Token> expectedTokens = List.of(
            new Token(TokenType.TEXT, "Howdy")
        );

        assertNotNull(tokens);
        assertThat(tokens)
            .containsExactlyElementsOf(expectedTokens);
    }

    @Test
    void assertThatParsingTextWithSimpleBackslashSucceeds() throws Exception {
        final List<Token> tokens = lexer.parse("Howdy\\");
        
        final List<Token> expectedTokens = List.of(
            new Token(TokenType.TEXT, "Howdy"),
            new Token(TokenType.TEXT, "\\")

        );

        assertNotNull(tokens);
        assertThat(tokens)
            .containsExactlyElementsOf(expectedTokens);
    }

    @Test
    void assertThatParsingTextWithBackslashesAndEscapesSucceeds() throws Exception {
        final List<Token> tokens = lexer.parse("Howdy \\ \\@ @W50 @W1.5 test");
        
        final List<Token> expectedTokens = List.of(
            new Token(TokenType.TEXT, "Howdy "),
            new Token(TokenType.TEXT, "\\"),
            new Token(TokenType.TEXT, " "),
            new Token(TokenType.TEXT, "@"),
            new Token(TokenType.TEXT, " "),
            new Token(TokenType.BUFFERED_COMMAND, "W50"),
            new Token(TokenType.TEXT, " "),
            new Token(TokenType.BUFFERED_COMMAND, "W1.5"),
            new Token(TokenType.TEXT, " test")

        );

        assertNotNull(tokens);
        assertThat(tokens)
            .containsExactlyElementsOf(expectedTokens);
    }
}
