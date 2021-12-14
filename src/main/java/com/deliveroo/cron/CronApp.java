package com.deliveroo.cron;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CronApp {

    ExpressionParserFactory expressionParserFactory = new ExpressionParserFactory();
    PatternTypeMatcher patternTypeMatcher = new PatternTypeMatcher();
    PatternValidator patternValidator = new PatternValidator();
    ParsingLogicFactory parsingLogicFactory = new ParsingLogicFactory(expressionParserFactory, patternTypeMatcher, patternValidator);

    ParsingLogic minutesParser = parsingLogicFactory.createParsingLogic( () ->
                    Stream
                            .iterate(0, number -> number < 60, number -> ++number)
                            .collect(Collectors.toList())
            );
    ParsingLogic hoursParser = parsingLogicFactory.createParsingLogic( () ->
                    Stream
                            .iterate(1, number -> number <= 24, number -> ++number)
                            .collect(Collectors.toList())
            );
    ParsingLogic daysOfWeekParser = parsingLogicFactory.createParsingLogic( () ->
            Stream
                    .iterate(1, number -> number <= 7, number -> ++number)
                    .collect(Collectors.toList())
    );
    ParsingLogic daysOfMonthParser = parsingLogicFactory.createParsingLogic( () ->
            Stream
                    .iterate(1, number -> number <= 31, number -> ++number)
                    .collect(Collectors.toList())
    );
    ParsingLogic monthsParser = parsingLogicFactory.createParsingLogic( () ->
            Stream
                    .iterate(1, number -> number <= 12, number -> ++number)
                    .collect(Collectors.toList())
    );

    public void evaluateCronExpressions(String arg) {
        String[] programArguments = arg.split("\\s+");
        if (programArguments.length < 6 ) {
            throw new IllegalArgumentException("Wrong number of arguments given");
        }
        String command = Arrays.stream(programArguments).skip(5).collect(Collectors.joining(" "));

        String minutesValues = minutesParser.parse(programArguments[1]);
        String hoursValues = hoursParser.parse(programArguments[2]);
        String daysOfMonthValues = daysOfMonthParser.parse(programArguments[3]);
        String daysOfWeekValues = daysOfMonthParser.parse(programArguments[3]);
        String monthValues = daysOfMonthParser.parse(programArguments[3]);

    }

    public static void main(String[] args) {
        CronApp cronApp = new CronApp();
        cronApp.evaluateCronExpressions(args[0]);
    }

    private class ParsingLogicFactory {
        private final ExpressionParserFactory expressionParserFactory;
        private final PatternTypeMatcher patternTypeMatcher;
        private final PatternValidator patternValidator;

        private ParsingLogicFactory(ExpressionParserFactory expressionParserFactory, PatternTypeMatcher patternTypeMatcher, PatternValidator patternValidator) {
            this.expressionParserFactory = expressionParserFactory;
            this.patternTypeMatcher = patternTypeMatcher;
            this.patternValidator = patternValidator;
        }


        public ParsingLogic createParsingLogic(Supplier<List<Integer>> valuesSupplier) {
            return new ParsingLogic(expressionParserFactory, patternTypeMatcher, valuesSupplier, patternValidator);
        }
    }

}
