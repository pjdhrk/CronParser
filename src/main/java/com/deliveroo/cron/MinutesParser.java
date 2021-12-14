package com.deliveroo.cron;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinutesParser extends ParsingLogic {

    private final static int MAX = 60;

    public MinutesParser(ExpressionParserFactory expressionParserFactory, PatternTypeMatcher patternValidator) {
        super(expressionParserFactory, patternValidator,
                () -> Stream
                .iterate( 0, integer -> ++integer)
                .limit(MAX)
                .collect(Collectors.toList()), patternValidator);
    }

    @Override
    void validate(String input) {
        List.of("(\\d{1,2}|\\*)(/\\d{1,2})?", "[(\\d{1,2}),]*", "\\d{1,2}-\\d{1,2}").stream()
                .map(Pattern::compile).map(pattern -> pattern.matcher(input))
                .map(Matcher::matches)
                .filter(Predicate.isEqual(Boolean.TRUE))
                .findAny().orElseThrow( () -> new RuntimeException("Wrong pattern given for minutes"));
    }
}
