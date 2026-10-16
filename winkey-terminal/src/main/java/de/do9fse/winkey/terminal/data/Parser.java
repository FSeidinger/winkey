package de.do9fse.winkey.terminal.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.do9fse.winkey.lib.core.model.commands.host.buffered.CancelBufferedSpeedCommand;
import de.do9fse.winkey.lib.core.model.commands.host.buffered.ChangeSpeedBufferedCommand;
import de.do9fse.winkey.lib.core.model.commands.host.buffered.HscwSpeedChangeCommand;
import de.do9fse.winkey.lib.core.model.commands.host.buffered.NopCommand;
import de.do9fse.winkey.lib.core.model.commands.host.buffered.PttCommand;
import de.do9fse.winkey.lib.core.model.commands.host.buffered.WaitCommand;
import de.do9fse.winkey.lib.core.model.configuration.CustomUnits;
import de.do9fse.winkey.lib.core.model.data.DataItem;
import de.do9fse.winkey.lib.core.model.data.TextDataItem;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

public class Parser {
    private final Lexer lexer;

    private Iterator<Token> tokens;

    public Parser() {
        this.lexer = new Lexer();
    }

    public List<DataItem> parse(final String line) throws IOException {
        this.tokens = lexer.parse(line).iterator();
        final List<DataItem> dataItems = new ArrayList<>();

        while (tokens.hasNext()) {
            final Token token = tokens.next();

            switch (token.tokenType()) {
                case TEXT:
                    dataItems.add(new TextDataItem(token.value()));
                    break;

                case BUFFERED_COMMAND_IDENTIFIER:
                    dataItems.add(parseCommand(token));
                    break;

                default:
                    throw new IllegalStateException("Unexpected token " + token.tokenType());
            }
        }

        return dataItems;
    }

    private DataItem parseCommand(final Token commandIdentifierToken) {
        return switch (commandIdentifierToken.value()) {
            case "c", "C" -> {
                    yield new CancelBufferedSpeedCommand();
                }

            case "l", "L" -> {
                    yield new HscwSpeedChangeCommand(Quantities.getQuantity(readIntParameter() * 100, CustomUnits.LPM));
                }

            case "n", "N" -> {
                    yield new NopCommand();
                }

            case "p", "P" -> {
                    yield new PttCommand(readBooleanParameter());
                }

            case "s", "S" -> {
                    yield new ChangeSpeedBufferedCommand(Quantities.getQuantity(readIntParameter(), CustomUnits.WPM));
                }

            case "w", "W" -> {
                    yield new WaitCommand(Quantities.getQuantity(readIntParameter(), Units.SECOND));
                }

            default -> throw new IllegalArgumentException("Unknown command identifier '" + commandIdentifierToken.value() + "'");
        };
    }

    private int readIntParameter() {
        if (!tokens.hasNext()) {
            throw new IllegalStateException("Expected integer parameter but got EOF");
        }

        final Token token = tokens.next();
        if (token.tokenType() != TokenType.BUFFERED_COMMAND_PARAMETER) {
            throw new IllegalStateException("Expected integer but got " + token.tokenType());
        }

        return Integer.parseInt(token.value());
    }

    private boolean readBooleanParameter() {
        if (!tokens.hasNext()) {
            throw new IllegalStateException("Expected boolean parameter but got EOF");
        }

        final Token token = tokens.next();
        if (token.tokenType() != TokenType.BUFFERED_COMMAND_PARAMETER) {
            throw new IllegalStateException("Expected boolean parameter but got " + token.tokenType());
        }

        final String value = token.value().toLowerCase();

        return switch (value) {
            case "true", "on", "1", "enabled", "yes" -> true;
            case "off", "0", "false", "disabled", "no" -> false;
            default -> throw new IllegalArgumentException("Invalid boolean parameter value: '" + token.value() + "'");
        };
    }
}
