package com.deliveroo.cron;

import com.deliveroo.cron.expressions.ExpressionParserFactory;
import com.deliveroo.cron.patterns.PatternType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpressionParserFactoryTest {

    ExpressionParserFactory expressionParserFactory = new ExpressionParserFactory();

    Supplier<List<Integer>> minutesSupplier = () -> Stream.iterate(0, value -> value < 60, value -> ++value ).collect(Collectors.toList());

    @Test
    void when_listed_given_then_should_list_only_given() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.LISTED);

        assertThat(patternHandler.willRunAt("1,5,11", minutesSupplier))
                .isEqualTo("1 5 11");

    }
    @Test
    void when_scope_given_then_should_return_all_in_scope() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.SCOPE);

        assertThat(patternHandler.willRunAt("5-11", minutesSupplier))
                .isEqualTo("5 6 7 8 9 10 11");

    }

    @Test
    void when_wildcard_given_then_should_return_all_values() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.EVERY);

        assertThat(patternHandler.willRunAt("*", minutesSupplier))
                .isEqualTo(Stream.iterate(0, element -> ++element).limit(60).map(String::valueOf).collect(Collectors.joining(" ")));

    }

    @Test
    void when_reccuring_with_wildcard_given_then_should_return_on_every_15_minutes() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.RECURRING);

        assertThat(patternHandler.willRunAt("*/15", minutesSupplier))
                .isEqualTo("0 15 30 45");

    }

    @Test
    void when_comma_delimitered_then_should_parse_listed() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.RECURRING);

        assertThat(patternHandler.willRunAt("5/15", minutesSupplier))
                .isEqualTo("5 20 35 50");

    }

    @Test
    void when_values_outside_of_scope_given_for_scope_then_should_throw_exception() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.SCOPE);

        assertThatThrownBy( () -> patternHandler.willRunAt("59-60", minutesSupplier))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Value out of scope. Given 60 allowed 59");
    }

    @Test
    void when_values_outside_of_scope_given_for_listed_then_should_throw_exception() {
        var patternHandler = expressionParserFactory.getPatternHandler(PatternType.LISTED);

        assertThatThrownBy( () -> patternHandler.willRunAt("1,15,65,20", minutesSupplier))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Value out of scope. Given 65 allowed 59");
    }
}