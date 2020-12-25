package org.levshunov.domino.model;

import java.util.*;

public class Domino {
    public static final int PRINTED_DOMINO_LENGTH = 5;

    private int firstNumber;
    private int secondNumber;

    public Domino(int firstNumber, int secondNumber) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
    }

    public int getFirstNumber() {
        return firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

    public int getPoints() {
        return firstNumber + secondNumber;
    }

    public void reverse() {
        int temp = firstNumber;
        firstNumber = secondNumber;
        secondNumber = temp;
    }

    public List<Integer> getPossiblePoints() {
        return Arrays.asList(firstNumber, secondNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domino domino = (Domino) o;
        return firstNumber == domino.firstNumber && secondNumber == domino.secondNumber
            || firstNumber == domino.secondNumber && secondNumber == domino.firstNumber;
    }

    @Override
    public String toString() {
        return "[" + firstNumber + "|" + secondNumber + "]";
    }

    public String toReversedString() {
        return "]" + firstNumber + "|" + secondNumber + "[";
    }
}
