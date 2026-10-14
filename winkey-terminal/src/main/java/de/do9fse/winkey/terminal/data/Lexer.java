package de.do9fse.winkey.terminal.data;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private static final int EOF = -1;
    
    public List<Token> parse(final String line) throws IOException {
        final List<Token> tokens = new ArrayList<>();

        if (line == null || line.isEmpty()) {
            return tokens;
        }

        final Reader reader = new StringReader(line);

        while (true) {
            reader.mark(1);
            final int nextChar = reader.read();

            switch (nextChar) {
                case '\\':
                    parseEscape(reader, tokens);
                    break;

                case '@':
                    parseBufferedCommand(reader, tokens);
                    break;

                case EOF:
                    return tokens;

                default:
                    reader.reset();
                    parseText(reader, tokens);
            }
        }
    }

    private void parseText(final Reader reader, final List<Token> tokens) throws IOException {
        final StringBuffer textBuffer = new StringBuffer();

        while (true) {
            reader.mark(1);
            final int nextChar = reader.read();

            if (nextChar == EOF || nextChar == '\\' || nextChar == '@') {
                reader.reset();
                break;
            }

            textBuffer.append((char) nextChar);
        }

        if (!textBuffer.isEmpty()) {
            tokens.add(new Token(TokenType.TEXT, textBuffer.toString()));
        }
    }

    private void parseBufferedCommand(final Reader reader, final List<Token> tokens) throws IOException {
        final StringBuilder cmdBuffer = new StringBuilder();

        while (true) {
            reader.mark(1);
            final int nextChar = reader.read();

            if (nextChar == EOF || nextChar == ' ' || nextChar == '@') {
                reader.reset();
                break;
            }

            cmdBuffer.append((char) nextChar);
        }

        tokens.add(new Token(TokenType.BUFFERED_COMMAND, cmdBuffer.toString()));
    }

    private void parseEscape(final Reader reader, final List<Token> tokens) throws IOException {
        reader.mark(1);
        final int lookahead = reader.read();

        final Token token = switch (lookahead) {
            case '@' -> new Token(TokenType.TEXT, "@");
            default -> {
                reader.reset();
                yield new Token(TokenType.TEXT, "\\");
            }
        };

        tokens.add(token);
    }
}
