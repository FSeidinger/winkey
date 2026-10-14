package de.do9fse.winkey.terminal.data;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private static final int EOF = -1;

    private Reader reader;
    private List<Token> tokens;
    
    public List<Token> parse(final String line) throws IOException {
        tokens = new ArrayList<>();

        if (line == null || line.isEmpty()) {
            return tokens;
        }

        reader = new StringReader(line);

        while (true) {
            if (isCommandIntro()) {
                parseBufferedCommand();
                continue;
            }

            final int nextData = reader.read();
            if (nextData == EOF) {
                return tokens;
            }

            final char nextChar = (char) nextData;

            switch (nextChar) {
                case '\\':
                    parseEscape(reader, tokens);
                    break;

                default:
                    reader.reset();
                    parseText(reader, tokens);
            }
        }
    }

    private boolean isCommandIntro() throws IOException {
        reader.mark(2);
        int data = reader.read();
        if (data == EOF) {
            return false;
        }

        final char lookAhead1 = (char) data;

        switch (lookAhead1) {
            case ' ':
                data = reader.read();
                if (data == EOF) {
                    return false;
                }

                final char lookAhead2 = (char) data;
                switch (lookAhead2) {
                    case '@':
                        return true;
                }
                break;

            case '@':
                return true;
        }

        reader.reset();
        return false;
    }

    private void parseBufferedCommand() throws IOException {
        parseCommandIdentifier();
        parseCommandParameter();
    }

    private void parseCommandIdentifier() throws IOException {
        final int data = reader.read();

        if (data == EOF) {
            throw new IllegalArgumentException("Reached EOF while reading command identifier");
        }

        final char nextChar = (char) data;
        if (!isAToZ(nextChar)) {
            throw new IllegalArgumentException("Invalid command identifier " + nextChar);
        }

        tokens.add(new Token(TokenType.BUFFERED_COMMAND_IDENTIFIER, String.valueOf(nextChar)));
    }

    private boolean isAToZ(char nextChar) {
        final char toCompare = Character.toUpperCase(nextChar);
        return toCompare >= 'A' && toCompare <= 'Z';

    }

    private void parseCommandParameter() throws IOException {
        final StringBuffer buffer = new StringBuffer();

        boolean ready = false;
        while (!ready) {
            reader.mark(1);
            final int data = reader.read();
            if (data == EOF) {
                ready = true;
                break;
            }

            final char nextChar = (char) data;
            switch (nextChar) {
                case ' ':
                    ready = true;
                    break;

                case '@':
                    reader.reset();
                    ready = true;
                    break;

                default:
                    buffer.append((char) nextChar);
            }
        }

        if (!buffer.isEmpty()) {
            tokens.add(new Token(TokenType.BUFFERED_COMMAND_PARAMETER, buffer.toString()));
        }
    }

    private void parseText(final Reader reader, final List<Token> tokens) throws IOException {
        final StringBuffer textBuffer = new StringBuffer();

        while (true) {
            reader.mark(1);

            final int data = reader.read();
            if (data == EOF) {
                reader.reset();
                break;
            }

            final char nextChar = (char) data;
            if (nextChar == '\\' || nextChar == '@') {
                reader.reset();
                break;
            }

            textBuffer.append((char) nextChar);
        }

        if (!textBuffer.isEmpty()) {
            tokens.add(new Token(TokenType.TEXT, textBuffer.toString()));
        }
    }

    private void parseEscape(final Reader reader, final List<Token> tokens) throws IOException {
        reader.mark(1);
        final int data = reader.read();
        if (data == EOF) {
            tokens.add(new Token(TokenType.TEXT, "\\"));
            return;
        }

        final char lookahead = (char) data;

        if (lookahead == '@') {
            tokens.add(new Token(TokenType.TEXT, "@"));
            return;
        }

        reader.reset();
        tokens.add(new Token(TokenType.TEXT, "\\"));
    }
}
