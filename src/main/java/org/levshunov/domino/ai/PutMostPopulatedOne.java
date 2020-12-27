package org.levshunov.domino.ai;

import org.levshunov.domino.model.Field;
import org.levshunov.domino.model.Hand;
import org.levshunov.domino.model.Turn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PutMostPopulatedOne implements AIStrategy {
    @Override
    public String uiName() {
        return "Положи самую частую цифру";
    }

    @Override
    public Turn makeTurn(List<Turn> possibleTurns, Field field, Hand myHand, Integer opponentHandCount) {
        int[] counts = new int[7];
        myHand.getDominoes().stream()
            .flatMap(domino ->  domino.getPossiblePoints().stream())
            .forEach(value -> counts[value]++);

        List<ValueCountPoints> valueCounts = new ArrayList<>();
        for (int i = 0; i < counts.length; i++) {
            valueCounts.add(new ValueCountPoints(i, counts[i]));
        }
        Collections.sort(valueCounts);
        for (int i = 0; i < valueCounts.size(); i++) {
            valueCounts.get(i).setPoints(i);
        }

        return possibleTurns.stream()
            .map(turn -> new TurnPointsPair(turn, valueCounts))
            .sorted()
            .findFirst().get()
            .getTurn();
    }

    private static class ValueCountPoints implements Comparable<ValueCountPoints> {
        private Integer dominoValue, count, points;

        public ValueCountPoints(int dominoValue, int count) {
            this.dominoValue = dominoValue;
            this.count = count;
        }

        public Integer getDominoValue() {
            return dominoValue;
        }

        public Integer getCount() {
            return count;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

        @Override
        public int compareTo(ValueCountPoints o) {
            if (this.count.equals(o.count)) {
                return o.dominoValue.compareTo(this.dominoValue);
            } else {
                return o.count.compareTo(this.count);
            }
        }
    }

    private static class TurnPointsPair implements Comparable<TurnPointsPair> {
        private Turn turn;
        private Integer points;

        public Turn getTurn() {
            return turn;
        }

        public TurnPointsPair(Turn turn, List<ValueCountPoints> valueCountPoints) {
            this.turn = turn;

            List<Integer> possiblePoints = turn.getDomino().getPossiblePoints();
            points = 0;
            for (ValueCountPoints valueCountPoint : valueCountPoints) {
                if (possiblePoints.contains(valueCountPoint.getDominoValue())) {
                    points += valueCountPoint.getPoints();
                    if (turn.getDomino().isDouble()) {
                        points += valueCountPoint.getPoints();
                    }
                }
            }
        }

        @Override
        public int compareTo(TurnPointsPair o) {
            if (this.points.equals(o.points)) {
                Integer points1 = this.turn.getDomino().getPoints();
                Integer points2 = o.turn.getDomino().getPoints();
                if (this.turn.getDomino().isDouble() && points1 == 0) {
                    return -1;
                } else if (o.turn.getDomino().isDouble() && points2 == 0) {
                    return 1;
                }
                return points2.compareTo(points1);
            }
            return this.points.compareTo(o.points);
        }
    }
}
