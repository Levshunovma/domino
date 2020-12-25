package org.levshunov.domino.ai;

import java.util.ArrayList;
import java.util.List;

public class StrategyHelper {
    private static List<AIStrategy> strategies;

    static {
        strategies = new ArrayList<>();
        strategies.add(new TakeRandomOne());
    }

    public static List<AIStrategy> getStrategies() {
        return strategies;
    }

    public static AIStrategy getStrategyByName(String name) {
        for (AIStrategy strategy : strategies) {
            if (strategy.getClass().getName().equals(name)) {
                return strategy;
            }
        }
        return null;
    }
}
