package org.levshunov.domino.ai;

import org.levshunov.domino.model.Field;
import org.levshunov.domino.model.Hand;
import org.levshunov.domino.model.Turn;

import java.util.List;

public class TakeRandomOne implements AIStrategy {
    @Override
    public String uiName() {
        return "Возьми первую доступную";
    }

    @Override
    public Turn makeTurn(Field field, Hand myHand, Hand opponentHand, List<Turn> possibleTurns) {
        return possibleTurns.get(0);
    }
}
