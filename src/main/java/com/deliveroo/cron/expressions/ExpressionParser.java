package com.deliveroo.cron.expressions;


import java.util.List;
import java.util.function.Supplier;

public interface ExpressionParser {

    String willRunAt(String definition, Supplier<List<Integer>> possibleValue);

}
