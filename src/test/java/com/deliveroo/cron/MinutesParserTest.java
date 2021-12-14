package com.deliveroo.cron;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MinutesParserTest {
    MinutesParser minutesParser = new MinutesParser(new ExpressionParserFactory(), new PatternTypeMatcher());

    @Test
    void shouldGetIt() {
        minutesParser.validate("*");
        minutesParser.validate("10/5");
        minutesParser.validate("*/5");
        minutesParser.validate("1,2,3");
        minutesParser.validate("100,2,3");
    }


}