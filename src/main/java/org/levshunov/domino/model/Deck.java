package org.levshunov.domino.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.levshunov.domino.Game.MAX_DOMINO;
import static org.levshunov.domino.Game.START_DECK_SIZE;

public class Deck {
    private List<Domino> dominoes;

    public Deck() {
        setNewDeck();
    }

    public void setNewDeck() {
        dominoes = new ArrayList<>();
        for (int i = 0; i <= MAX_DOMINO; i++) {
            for (int j = i; j <= MAX_DOMINO; j++) {
                dominoes.add(new Domino(i, j));
            }
        }
        Random random = new Random();
        Collections.shuffle(dominoes, random);
    }

    public boolean hasNext() {
        return !dominoes.isEmpty();
    }

    public Domino getNext() {
        if (dominoes.isEmpty()) {
            return null;
        }
        Domino domino = dominoes.get(dominoes.size() - 1);
        dominoes.remove(dominoes.size() - 1);
        return domino;
    }

    public List<Domino> getNext(int count) {
        if (dominoes.size() < count) {
            return null;
        }
        List<Domino> dominoList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dominoList.add(getNext());
        }
        return dominoList;
    }

    public Integer getDominoesCount() {
        return dominoes.size();
    }

    public List<Domino> makeStartHand() {
        return getNext(START_DECK_SIZE);
    }

    public static Integer maxDeckSize() {
        return (MAX_DOMINO + 2) * (MAX_DOMINO + 1) / 2;
    }
}
