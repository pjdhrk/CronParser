package com.deliveroo.cron.expressions;

import com.deliveroo.cron.patterns.PatternType;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpressionParserFactory {

    private static final String DELIMITER = " ";

        public ExpressionParser getPatternHandler(PatternType patternType) {
            return switch (patternType) {
                case EVERY -> (definition, possibleValuesSupplier) -> possibleValuesSupplier.get().stream().map(String::valueOf).collect(Collectors.joining(DELIMITER));
                case LISTED -> (definition, possibleValuesSupplier) -> {
                    List<Integer> possibleValues = possibleValuesSupplier.get();
                    int min = possibleValues.get(0);
                    int max = possibleValues.get(possibleValues.size() - 1);
                    return Pattern.compile(",").splitAsStream(definition)
                            .map(Integer::valueOf)
                            .sorted()
                            .peek(value -> checkMax(value, max))
                            .peek(value -> checkMin(value, min))
                            .map(String::valueOf)
                            .collect(Collectors.joining(DELIMITER));
                };
                case RECURRING -> (definition, possibleValuesSupplier) -> {
                    List<Integer> possibleValues = possibleValuesSupplier.get();
                    int min = possibleValues.get(0);
                    int max = possibleValues.get(possibleValues.size() - 1);
                    String[] split = definition.split("/");
                    Integer divider = split[0].equals("*") ? min : Integer.parseInt(split[0]);
                    checkMin(divider, min);
                    Integer divisor = Integer.valueOf(split[1]);
                    checkMax(divisor, max / 2);
                    return Stream.iterate(divider, result -> result < max, element -> element + divisor)
                            .map(String::valueOf)
                            .collect(Collectors.joining(DELIMITER));

                };
                case SCOPE -> (definition, possibleValuesSupplier) -> {
                    List<Integer> possibleValues = possibleValuesSupplier.get();
                    String[] split = definition.split("-");
                    Integer from = Integer.valueOf(split[0]);
                    Integer to = Integer.valueOf(split[1]);
                    int min = possibleValues.get(0);
                    int max = possibleValues.get(possibleValues.size() - 1);
                    checkMin(from, min);
                    checkMax(to, max);
                    return Stream.iterate(from, result -> result <= to, element -> ++element)
                            .map(String::valueOf)
                            .collect(Collectors.joining(DELIMITER));
                };
            };
        }

    private void checkMin(Integer given, int expected) {
        if (given < expected ) {
            throw new IllegalArgumentException("Value out of scope. Given " + given + " allowed " + expected);
        }
    }

    private void checkMax(Integer given, int expected) {
        if (given > expected ) {
            throw new IllegalArgumentException("Value out of scope. Given " + given + " allowed " + expected);
        }
    }

}
