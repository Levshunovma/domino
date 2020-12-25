package org.levshunov.domino.ai;

import org.levshunov.domino.model.Field;
import org.levshunov.domino.model.Hand;
import org.levshunov.domino.model.Turn;

import java.util.List;

// It's expected that there's at least one possible turn
public interface AIStrategy {
    String uiName();
    Turn makeTurn(Field field, Hand myHand, Hand opponentHand, List<Turn> possibleTurns);
}
