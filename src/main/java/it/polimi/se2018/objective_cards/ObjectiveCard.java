package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;

import java.io.Serializable;

public interface ObjectiveCard extends Serializable {

    int getPoints(PlayerBoard playerBoard);
}
