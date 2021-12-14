package com.deliveroo.cron;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CronAppTest {


    @Test
    void initialTest() {
        System.out.println(new CronApp().evaluateCronExpressions("*/15 0 1,15 * 1-5 /usr/bin/find -R"));
    }

}