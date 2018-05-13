package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.Color;

public class PrivateObjective implements ObjectiveCard {

    private Color color;

    public PrivateObjective(Color color) {
        this.color = color;
    }

    @Override
    public int getPoints(PlayerBoard playerBoard) {
        return 0;
    }
}
