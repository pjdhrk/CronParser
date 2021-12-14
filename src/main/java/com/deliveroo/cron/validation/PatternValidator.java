package com.deliveroo.cron.validation;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PatternValidator {

    public void validate(String input) {
        Stream.of("(\\d{1,2}|\\*)(/\\d{1,2})?", "[(\\d{1,2}),]*", "\\d{1,2}-\\d{1,2}")
                .map(Pattern::compile).map(pattern -> pattern.matcher(input))
                .map(Matcher::matches)
                .filter(Predicate.isEqual(Boolean.TRUE))
                .findAny().orElseThrow( () -> new RuntimeException("Unsupported expression given " + input));
    }
}
