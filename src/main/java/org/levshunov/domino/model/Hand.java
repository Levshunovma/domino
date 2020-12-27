package org.levshunov.domino.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.levshunov.domino.Game.MAX_DOMINO;

public class Hand {
    private List<Domino> dominoes;

    public Hand(List<Domino> dominoes) {
        this.dominoes = dominoes;
    }

    public List<Domino> getDominoes() {
        return dominoes;
    }

    public void takeDomino(Domino domino) {
        dominoes.add(domino);
    }

    public void playDomino(Domino domino) {
        dominoes.remove(domino);
    }

    public int getPoints() {
        if (dominoes.size() == 1 && new Domino(0, 0).equals(dominoes.get(0))) {
            return 25;
        }
        return dominoes.stream()
            .map(Domino::getPoints)
            .reduce(Integer::sum)
            .orElse(0);
    }

    public List<Turn> getPossibleTurns(Field field) {
        List<Turn> turnsList = dominoes.stream()
            .filter(domino -> domino.getPossiblePoints().stream()
                .anyMatch(point -> point.equals(field.leftPoints())))
            .map(domino -> new Turn(domino, Side.LEFT))
            .collect(Collectors.toList());

        turnsList.addAll(dominoes.stream()
            .filter(domino -> domino.getPossiblePoints().stream()
                .anyMatch(point -> point.equals(field.rightPoints())))
            .map(domino -> new Turn(domino, Side.RIGHT))
            .collect(Collectors.toList()));

        return turnsList;
    }

    public List<Turn> getPossibleTurns() {
        return dominoes.stream()
            .filter(domino -> domino.getFirstNumber().equals(domino.getSecondNumber()) && domino.getFirstNumber() != 0)
            .sorted(Comparator.comparing(Domino::getFirstNumber))
            .map(domino -> new Turn(domino, Side.FIRST_TURN))
            .collect(Collectors.toList());
    }

    public List<Turn> getPossibleTurns(int minPoints) {
        return dominoes.stream()
            .filter(domino -> domino.getPoints() == minPoints)
            .map(domino -> new Turn(domino, Side.FIRST_TURN))
            .collect(Collectors.toList());
    }

    public int findMinimumPoints() {
        return dominoes.stream()
            .map(Domino::getPoints)
            .sorted()
            .findFirst()
            .orElse(MAX_DOMINO * 2 + 1);
    }

    public Integer getDominoesCount() {
        return dominoes.size();
    }

    public String getUIList() {
        StringBuilder uiList = new StringBuilder();
        uiList.append("<html>");
        for (int i = 0; i < dominoes.size(); i++) {
            if (i > 0 && i % 7 == 0) {
                uiList.append("<br>");
            }
            uiList.append(dominoes.get(i).toString());
            uiList.append("&nbsp;");
        }
        uiList.append("</html>");
        return uiList.toString();
    }
}
