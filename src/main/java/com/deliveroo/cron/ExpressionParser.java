package com.deliveroo.cron;


import java.util.List;
import java.util.function.Supplier;

public interface ExpressionParser {

    String willRunAt(String definition, Supplier<List<Integer>> possibleValue);

}
