package com.deliveroo.cron;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

}