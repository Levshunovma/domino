package org.levshunov.domino.model;

import java.util.ArrayDeque;
import java.util.Deque;

public class Field {
    public static final Field EMPTY_FIELD = new Field();
    private static final int PRINTED_FIELD_LENGTH = 10;

    private Deque<Domino> dominoes;

    public Field(Domino domino) {
        dominoes = new ArrayDeque<>();
        dominoes.add(domino);
    }

    private Field() {
        dominoes = new ArrayDeque<>();
    }

    public Deque<Domino> getDominoes() {
        return dominoes;
    }

    public void addLeft(Domino domino) {
        if (domino.getFirstNumber() == leftPoints()) {
            domino.reverse();
        }
        dominoes.addFirst(domino);
    }

    public void addRight(Domino domino) {
        if (domino.getSecondNumber() == rightPoints()) {
            domino.reverse();
        }
        dominoes.addLast(domino);
    }

    public int leftPoints() {
        return dominoes.getFirst().getFirstNumber();
    }

    public int rightPoints() {
        return dominoes.getLast().getSecondNumber();
    }

    public String printField() {
        StringBuilder uiList = new StringBuilder();
        StringBuilder line = new StringBuilder();
        int index = 0;
        boolean isReverseOrder = false;
        for (Domino domino : dominoes) {
            if (index > 0 && index % PRINTED_FIELD_LENGTH == 0) {
                if (isReverseOrder) {
                    line.reverse();
                }
                line.append("\n");
                uiList.append(line);
                if (isReverseOrder) {
                    uiList.append(" |\n");
                } else {
                    uiList.append(" ".repeat(
                        PRINTED_FIELD_LENGTH * (Domino.PRINTED_DOMINO_LENGTH) - Domino.PRINTED_DOMINO_LENGTH / 2))
                        .append("|\n");
                }
                isReverseOrder = !isReverseOrder;
                line = new StringBuilder();
            }
            if (isReverseOrder) {
                line.append(domino.toReversedString());
            } else {
                line.append(domino.toString());
            }
            index++;
        }
        if (isReverseOrder) {
            if (index % PRINTED_FIELD_LENGTH != 0) {
                line.append(" ".repeat(
                    (PRINTED_FIELD_LENGTH - index % PRINTED_FIELD_LENGTH) * Domino.PRINTED_DOMINO_LENGTH));
            }
            line.reverse();
        }
        line.append("\n");
        uiList.append(line);
        return uiList.toString();
    }
}
