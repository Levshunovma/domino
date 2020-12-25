package org.levshunov.domino.model;

public enum Side {
    LEFT("Налево"), RIGHT("Направо"), FIRST_TURN("");

    private String uiName;

    Side(String uiName) {
        this.uiName = uiName;
    }

    @Override
    public String toString() {
        return uiName;
    }
}
