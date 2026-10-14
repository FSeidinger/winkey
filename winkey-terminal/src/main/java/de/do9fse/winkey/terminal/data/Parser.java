package de.do9fse.winkey.terminal.data;

import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;
import javax.measure.quantity.Time;

import de.do9fse.winkey.lib.core.model.commands.host.buffered.WaitCommand;
import de.do9fse.winkey.lib.core.model.data.DataItem;
import de.do9fse.winkey.lib.core.model.data.TextDataItem;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

public class Parser {
    private final Lexer lexer;
    private final Locale locale;

    public final Pattern commandPattern;

    public Parser() {
        this(Locale.US);
    }

    public Parser(final Locale locale) {
        this.lexer = new Lexer();
        this.locale = locale;

        final char decimalSeparator = DecimalFormatSymbols.getInstance(locale).getDecimalSeparator();
        this.commandPattern = Pattern.compile("(?<command>[a-zA-Z])(?<value>[+-]?\\d+(\\" + decimalSeparator + "\\d+)?)(?<unit>[a-zA-ZµpP]*)");
    }

    public List<DataItem> parse(final String line) throws IOException {
        final List<DataItem> dataItems = new ArrayList<>();
        final List<Token> tokens = lexer.parse(line);

        for (final Token token: tokens) {
            final DataItem dataItem = switch (token.token()) {
                case TEXT -> new TextDataItem(token.value());
                case BUFFERED_COMMAND -> parseCommand(token.value(), locale);
            };

            dataItems.add(dataItem);
        }

        return dataItems;
    }

    private DataItem parseCommand(final String commandString, final Locale locale) {
        final Matcher matcher = commandPattern.matcher(commandString);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Illegal command format - " + commandString);
        }

        try {
            final String commandIdentifier = matcher.group("command");
            final NumberFormat format = NumberFormat.getInstance(locale);
            final Number number = format.parse(matcher.group("value"));

            final Unit<?> unit = parseUnit(matcher.group("unit"));

            return switch (commandIdentifier) {
                case "w", "W" -> {
                        final Unit<Time> duration = toTime(unit);
                        yield new WaitCommand(Quantities.getQuantity(number, duration));
                    }

                default -> throw new IllegalArgumentException("Unknown command identifier '" + commandIdentifier + "'");
            };
        } catch (final ParseException e) {
            throw new IllegalArgumentException("Parameter " + matcher.group("value" + "is not a valid number"));
        }
    }

    private Unit<Time> toTime(final Unit<?> unit) {
        try {
            if (AbstractUnit.ONE.isCompatible(unit)) {
                return Units.SECOND;
            }

            return unit.asType(Time.class);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException("Unit " + unit + " is not compatible with " + Time.class.getSimpleName());
        }
    }

    private Unit<?> parseUnit(final String unitString) {
        try {
            return AbstractUnit.parse(unitString);
        } catch (final MeasurementParseException e) {
            throw new IllegalArgumentException("Your unit " + unitString + " is not a valid SI unit");
        }
    }
}
