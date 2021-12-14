package com.deliveroo.cron;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpressionParserFactory {

    private static final String DELIMITER = " ";

        public ExpressionParser getPatternHandler(PatternType patternType) {
            switch (patternType) {
                case EVERY:
                    return (definition, possibleValue) -> possibleValue.get().stream().map(String::valueOf).collect(Collectors.joining(DELIMITER));
                case LISTED:
                    return (definition, possibleValue) -> Pattern.compile(",").splitAsStream(definition).collect(Collectors.joining(DELIMITER));
                case RECURRING:
                    return (definition, possibleValuesSupplier) -> {
                        List<Integer> possibleValues = possibleValuesSupplier.get();
                        int min = possibleValues.get(0);
                        int max = possibleValues.get(possibleValues.size()-1);
                        String[] split = definition.split("/");
                        Integer divider = split[0].equals("*") ? min : Integer.valueOf(split[0]);
                        Integer divisor = Integer.valueOf(split[1]);
                        return Stream.iterate(divider, result -> result < max, element -> element + divisor)
                                .map(String::valueOf)
                                .collect(Collectors.joining(DELIMITER));

                    };
                case SCOPE:
                    return (definition, possibleValue) -> {
                        String[] split = definition.split("-");
                        Integer min = Integer.valueOf(split[0]);
                        Integer max = Integer.valueOf(split[1]);
                        return Stream.iterate(min, result -> result <= max, element -> ++element)
                                .map(String::valueOf)
                                .collect(Collectors.joining(DELIMITER));
                    };
                default:
                    throw new IllegalArgumentException("Pattern " + patternType + " not handled");
            }
        }

}
