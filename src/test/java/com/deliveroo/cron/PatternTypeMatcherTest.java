package com.deliveroo.cron;


import com.deliveroo.cron.patterns.PatternFormatException;
import com.deliveroo.cron.patterns.PatternType;
import com.deliveroo.cron.patterns.PatternTypeMatcher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PatternTypeMatcherTest {

    PatternTypeMatcher patternTypeMatcher = new PatternTypeMatcher();

    @Test
    void scoped_pattern_should_be_detected() {
        assertThat(patternTypeMatcher.determinePatternType("20-50")).isEqualTo(PatternType.SCOPE);
    }

    @Test
    void every_pattern_should_be_detected() {
        assertThat(patternTypeMatcher.determinePatternType("*")).isEqualTo(PatternType.EVERY);
    }

    @Test
    void recurring_every_pattern_should_be_detected() {
        assertThat(patternTypeMatcher.determinePatternType("*/15")).isEqualTo(PatternType.RECURRING);
    }

    @Test
    void recurring_pattern_should_be_detected() {
        assertThat(patternTypeMatcher.determinePatternType("0/30")).isEqualTo(PatternType.RECURRING);
    }

    @Test
    void listed_pattern_should_be_detected() {
        assertThat(patternTypeMatcher.determinePatternType("1,3,4,5")).isEqualTo(PatternType.LISTED);
    }

    @Test
    void unknown_pattern_should_throw_exception() {
        assertThatThrownBy( () -> patternTypeMatcher.determinePatternType("-1/3")).isInstanceOf(PatternFormatException.class);
    }

}