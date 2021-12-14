package com.deliveroo.cron;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionParserFactoryTest {

    ExpressionParserFactory expressionParserFactory = new ExpressionParserFactory();

    Supplier<List<Integer>> minutesSupplier = () -> Stream.iterate(0, value -> value < 60, value -> ++value ).collect(Collectors.toList());

    @Test
    void patternTypeForListed() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.LISTED);

        assertThat(patternHandler.willRunAt("1,5,11", minutesSupplier))
                .isEqualTo("1,5,11");

    }
    @Test
    void patternTypeForScoped() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.SCOPE);

        assertThat(patternHandler.willRunAt("5-11", minutesSupplier))
                .isEqualTo("5,6,7,8,9,10,11");

    }

    @Test
    void patternTypeForEvery() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.EVERY);

        assertThat(patternHandler.willRunAt("*", minutesSupplier))
                .isEqualTo("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39," +
                        "40,41,42,43,44,45,46,47,48,49," +
                        "50,51,52,53,54,55,56,57,58,59");

    }

    @Test
    void patternTypeForRecurringWildcard() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.RECURRING);

        assertThat(patternHandler.willRunAt("*/15", minutesSupplier))
                .isEqualTo("0,15,30,45");

    }

    @Test
    void patternTypeForRecurringStart() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.RECURRING);

        assertThat(patternHandler.willRunAt("5/15", minutesSupplier))
                .isEqualTo("5,20,35,50");

    }

}