package it.polimi.se2018.objective_cards;

public enum PublicObjectiveName {
    COLORIDIVERSIRIGA("Colori diversi - riga");

    private String toolName;

    PublicObjectiveName(String str) {
        toolName = str;
    }

    @Override
    public String toString() {
        return toolName;
    }
}
