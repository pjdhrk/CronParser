package com.deliveroo.cron;

import com.deliveroo.cron.expressions.ExpressionParserFactory;
import com.deliveroo.cron.patterns.PatternTypeMatcher;
import com.deliveroo.cron.validation.PatternValidator;

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
                            .iterate(0, number -> number < 24, number -> ++number)
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

    public String evaluateCronExpressions(String arg) {
        String[] programArguments = arg.split("\\s+");
        if (programArguments.length < 6 ) {
            throw new IllegalArgumentException("Wrong number of arguments given");
        }
        String command = Arrays.stream(programArguments).skip(5).collect(Collectors.joining(" "));

        String minutesValues = minutesParser.parse(programArguments[0]);
        String hoursValues = hoursParser.parse(programArguments[1]);
        String daysOfMonthValues = daysOfMonthParser.parse(programArguments[2]);
        String monthValues = monthsParser.parse(programArguments[3]);
        String daysOfWeekValues = daysOfWeekParser.parse(programArguments[4]);


        return String.format("""
                        minute        %s
                        hour          %s
                        day of month  %s
                        month         %s
                        day of week   %s
                        command       %s""",
                minutesValues,
                hoursValues,
                daysOfMonthValues,
                monthValues,
                daysOfWeekValues,
                command
        );

    }

    public static void main(String[] args) {
        CronApp cronApp = new CronApp();
        if (args.length != 1 ) {
            printUsage();
            return;
        }
        try {
            String result = cronApp.evaluateCronExpressions(args[0]);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void printUsage() {
        System.out.print("""
                Usage: java -jar Deliveroo-1.0.jar "minutes hours daysOfMonth months daysOfWeek command"
                """);
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
