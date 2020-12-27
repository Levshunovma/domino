package org.levshunov.domino.ai;

import org.levshunov.domino.model.Field;
import org.levshunov.domino.model.Hand;
import org.levshunov.domino.model.Turn;

import java.util.List;

public class PutRandomOne implements AIStrategy {
    @Override
    public String uiName() {
        return "Положи первую доступную";
    }

    @Override
    public Turn makeTurn(List<Turn> possibleTurns, Field field, Hand myHand, Integer opponentHandCount) {
        return possibleTurns.get(0);
    }
}
