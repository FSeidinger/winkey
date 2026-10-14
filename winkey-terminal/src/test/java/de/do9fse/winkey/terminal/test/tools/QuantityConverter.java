package de.do9fse.winkey.terminal.test.tools;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import tech.units.indriya.AbstractUnit;
import tech.units.indriya.quantity.Quantities;

public class QuantityConverter extends SimpleArgumentConverter {
    private static final Pattern PATTERN = Pattern.compile("(?<value>[+-]?\\d+(\\.\\d+)?)\\s+(?<unit>[a-zA-ZµpP]*)");

    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        if (!(source instanceof String text)) {
            throw new ArgumentConversionException("Can only convert Strings, but was: " + (source != null ? source.getClass().getName() : "null"));
        }

        if (!Quantity.class.isAssignableFrom(targetType)) {
            throw new ArgumentConversionException("Target type must be a subtype of Quantity, but was: " + targetType.getName());
        }

        Matcher matcher = PATTERN.matcher(text.trim());
        if (!matcher.matches()) {
            throw new ArgumentConversionException("Invalid quantity format: " + text);
        }

        try {
            final String numberStr = matcher.group("value");
            final String unitStr = matcher.group("unit");

            final NumberFormat format = NumberFormat.getInstance(Locale.US);
            final Number value = format.parse(numberStr);

            Unit<?> unit = (unitStr == null || unitStr.isBlank()) 
                    ? AbstractUnit.ONE 
                    : AbstractUnit.parse(unitStr);

            return Quantities.getQuantity(value, unit);
        } catch (Exception e) {
            throw new ArgumentConversionException("Failed to parse quantity string: " + text, e);
        }
    }
}
