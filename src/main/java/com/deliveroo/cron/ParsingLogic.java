package com.deliveroo.cron;

import com.deliveroo.cron.expressions.ExpressionParser;
import com.deliveroo.cron.expressions.ExpressionParserFactory;
import com.deliveroo.cron.patterns.PatternType;
import com.deliveroo.cron.patterns.PatternTypeMatcher;
import com.deliveroo.cron.validation.PatternValidator;

import java.util.List;
import java.util.function.Supplier;

public class ParsingLogic {

    private final ExpressionParserFactory expressionParserFactory;
    private final PatternTypeMatcher patternTypeMatcher;
    private final Supplier<List<Integer>> possibleValuesSupplier;
    private final PatternValidator patternValidator;

    protected ParsingLogic(ExpressionParserFactory expressionParserFactory, PatternTypeMatcher patternTypeMatcher, Supplier<List<Integer>> possibleValuesSupplier, PatternValidator patternValidator) {
        this.expressionParserFactory = expressionParserFactory;
        this.patternTypeMatcher = patternTypeMatcher;
        this.possibleValuesSupplier = possibleValuesSupplier;
        this.patternValidator = patternValidator;
    }

    public String parse(String input) {
        validate(input);
        PatternType patternType = detectPatternType(input);
        return getPatternHandler(patternType).willRunAt(input, possibleValuesSupplier);
    }

    protected void validate(String input) {
        patternValidator.validate(input);
    }

    protected PatternType detectPatternType(String input) {
       return patternTypeMatcher.determinePatternType(input);
    }

    protected ExpressionParser getPatternHandler(PatternType patternType) {
        return expressionParserFactory.getPatternHandler(patternType);
    }

}
