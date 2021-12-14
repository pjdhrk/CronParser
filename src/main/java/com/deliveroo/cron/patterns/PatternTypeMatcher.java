package com.deliveroo.cron.patterns;

import java.util.List;
import java.util.regex.Pattern;

public class PatternTypeMatcher {

    private final List<RegexpPatternType> patternTypeMap = List.of(
            new RegexpPatternType(Pattern.compile("[(\\d{1,2})]*"), PatternType.LISTED),
            new RegexpPatternType(Pattern.compile("\\d{1,2}-\\d{1,2}"), PatternType.SCOPE),
            new RegexpPatternType(Pattern.compile("(\\d{1,2}|\\*)/\\d{1,2}"), PatternType.RECURRING),
            new RegexpPatternType(Pattern.compile("\\*"), PatternType.EVERY)
            );

    public PatternType determinePatternType(String input) {
        return patternTypeMap.stream().filter( pattern -> pattern.pattern().matcher(input).matches()).findFirst()
                .map(RegexpPatternType::patternType)
                .orElseThrow(() -> new PatternFormatException("Wrong pattern given " + input));
    }

    record RegexpPatternType (Pattern pattern, PatternType patternType){}
}
