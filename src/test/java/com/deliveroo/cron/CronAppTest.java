package com.deliveroo.cron;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CronAppTest {


    @Test
    void initialTest() {
        String expected = """
                        minute        0 15 30 45
                        hour          0
                        day of month  1 15
                        month         1 2 3 4 5 6 7 8 9 10 11 12
                        day of week   1 2 3 4 5
                        command       /usr/bin/find -R""";
        assertThat(new CronApp().evaluateCronExpressions("*/15 0 1,15 * 1-5 /usr/bin/find -R"))
                .isEqualTo(expected);
    }

}