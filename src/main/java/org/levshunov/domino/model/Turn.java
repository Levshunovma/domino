package org.levshunov.domino.model;

public class Turn {
    private Domino domino;
    private Side side;

    public Turn(Domino domino, Side side) {
        this.domino = domino;
        this.side = side;
    }

    public Domino getDomino() {
        return domino;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public String toString() {
        return domino.toString() + " " + side.toString();
    }
}
