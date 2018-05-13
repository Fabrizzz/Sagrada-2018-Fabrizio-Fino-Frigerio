package it.polimi.se2018.objective_cards;

import it.polimi.se2018.utils.Tool;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
