package org.levshunov.domino;

import org.levshunov.domino.ai.AIStrategy;
import org.levshunov.domino.model.*;

import java.util.List;
import java.util.Objects;

public class Game {
    public static final int MAX_DOMINO = 6;
    public static final int START_DECK_SIZE = 7;

    private Deck deck;
    private Field field;
    private Hand humanPlayer;
    private Hand aiPlayer;
    private AIStrategy aiStrategy;
    private Turn lastTurn;

    public Game(AIStrategy aiStrategy) {
        this.aiStrategy = aiStrategy;
        deck = new Deck();
        humanPlayer = new Hand(deck.makeStartHand());
        aiPlayer = new Hand(deck.makeStartHand());
    }

    public Deck getDeck() {
        return deck;
    }

    public Field getField() {
        if (field == null) {
            return Field.EMPTY_FIELD;
        }
        return field;
    }

    public Hand getHumanPlayer() {
        return humanPlayer;
    }

    public Hand getAiPlayer() {
        return aiPlayer;
    }

    public List<Turn> play(Turn turn) throws EndGameException {
        applyTurn(turn, humanPlayer);
        return play();
    }

    public List<Turn> play() throws EndGameException {
        if (field == null) {
            int whoGoesFirst = whoGoesFirst();
            if (whoGoesFirst > 0) {
                if (whoGoesFirst == 13) {
                    return humanPlayer.getPossibleTurns();
                } else {
                    return humanPlayer.getPossibleTurns(whoGoesFirst);
                }
            } else {
                List<Turn> aiPossibleTurns;
                if (whoGoesFirst == -13) {
                    aiPossibleTurns = aiPlayer.getPossibleTurns();
                } else {
                    aiPossibleTurns = aiPlayer.getPossibleTurns(-whoGoesFirst);
                }
                Turn aiTurn = aiStrategy.makeTurn(aiPossibleTurns, Field.EMPTY_FIELD, aiPlayer,
                    humanPlayer.getDominoesCount());
                applyTurn(aiTurn, aiPlayer);
            }
        } else {
            pickUpDominoes(aiPlayer);
            if (hasPossibleTurn(aiPlayer)) {
                Turn aiTurn = aiStrategy.makeTurn(aiPlayer.getPossibleTurns(field), field, aiPlayer,
                    humanPlayer.getDominoesCount());
                applyTurn(aiTurn, aiPlayer);
            } else if (lastTurn == null) {
                throw new EndGameException();
            } else {
                lastTurn = null;
            }
        }

        pickUpDominoes(humanPlayer);
        if (hasPossibleTurn(humanPlayer)) {
            return humanPlayer.getPossibleTurns(field);
        } else if (lastTurn == null) {
            throw new EndGameException();
        } else {
            lastTurn = null;
            return play();
        }
    }

    public int isHumanWin() {
        if (humanPlayer.getDominoes().size() == 0) {
            return 1;
        }
        if (aiPlayer.getDominoes().size() == 0) {
            return -1;
        }

        int humanPoints = humanPlayer.getPoints();
        int aiPoints = aiPlayer.getPoints();
        return -Objects.compare(humanPoints, aiPoints, Integer::compareTo);
    }

    private void applyTurn(Turn turn, Hand hand) throws EndGameException {
        if (Side.FIRST_TURN.equals(turn.getSide())) {
            field = new Field(turn.getDomino());
        } else if (Side.LEFT.equals(turn.getSide())) {
            field.addLeft(turn.getDomino());
        } else if (Side.RIGHT.equals(turn.getSide())) {
            field.addRight(turn.getDomino());
        }
        lastTurn = turn;
        hand.playDomino(turn.getDomino());
        if (hand.getDominoesCount() == 0) {
            throw new EndGameException();
        }
    }

    // positive - human, negative - ai;
    // 1 to 12 - minimum points to start field
    // 13 - start with double
    private int whoGoesFirst() {
        List<Turn> humanTurns = humanPlayer.getPossibleTurns();
        List<Turn> aiTurns = aiPlayer.getPossibleTurns();

        if (humanTurns.size() > 0) {
            if (aiTurns.size() == 0 ||
                humanTurns.get(0).getDomino().getFirstNumber() < aiTurns.get(0).getDomino().getFirstNumber()) {
                return 13;
            }
        }
        if (aiTurns.size() > 0) {
            return -13;
        }

        int humanPlayerMinimumPoints = humanPlayer.findMinimumPoints();
        int aiPlayerMinimumPoints = aiPlayer.findMinimumPoints();
        if (humanPlayerMinimumPoints < aiPlayerMinimumPoints) {
            return humanPlayerMinimumPoints;
        } else {
            return -aiPlayerMinimumPoints;
        }
    }

    private boolean hasPossibleTurn(Hand hand) {
        return !hand.getPossibleTurns(field).isEmpty();
    }

    private void pickUpDominoes(Hand hand) {
        while (!hasPossibleTurn(hand) && deck.hasNext()) {
            hand.takeDomino(deck.getNext());
        }
    }
}
