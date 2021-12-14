package com.deliveroo.cron;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternValidator {

    public void validate(String input) {
        List.of("(\\d{1,2}|\\*)(/\\d{1,2})?", "[(\\d{1,2}),]*", "\\d{1,2}-\\d{1,2}").stream()
                .map(Pattern::compile).map(pattern -> pattern.matcher(input))
                .map(Matcher::matches)
                .filter(Predicate.isEqual(Boolean.TRUE))
                .findAny().orElseThrow( () -> new RuntimeException("Unsupported expression given " + input));
    }
}
