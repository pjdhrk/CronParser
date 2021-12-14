package com.deliveroo.cron;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpressionParserFactory {

        public ExpressionParser getPatternHandler(PatternType patternType) {
            switch (patternType) {
                case EVERY:
                    return (definition, possibleValue) -> possibleValue.get().stream().map(String::valueOf).collect(Collectors.joining(","));
                case LISTED:
                    return (definition, possibleValue) -> definition;
                case RECURRING:
                    return (definition, possibleValuesSuplier) -> {
                        List<Integer> possibleValues = possibleValuesSuplier.get();
                        int min = possibleValues.get(0);
                        int max = possibleValues.get(possibleValues.size()-1);
                        String[] split = definition.split("/");
                        Integer divider = split[0].equals("*") ? min : Integer.valueOf(split[0]);
                        Integer divisor = Integer.valueOf(split[1]);
                        return Stream.iterate(divider, result -> result < max, element -> element + divisor)
                                .map(String::valueOf)
                                .collect(Collectors.joining(","));

                    };
                case SCOPE:
                    return (definition, possibleValue) -> {
                        String[] split = definition.split("-");
                        Integer min = Integer.valueOf(split[0]);
                        Integer max = Integer.valueOf(split[1]);
                        return Stream.iterate(min, result -> result <= max, element -> ++element)
                                .map(String::valueOf)
                                .collect(Collectors.joining(","));
                    };
                default:
                    throw new IllegalArgumentException("Pattern " + patternType + " not handled");
            }
        }

}
