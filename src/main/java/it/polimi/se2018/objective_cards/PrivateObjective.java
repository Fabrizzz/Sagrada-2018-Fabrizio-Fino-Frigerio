package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

public class PrivateObjective implements ObjectiveCard {

    private Color color;

    public PrivateObjective(Color color) {
        this.color = color;
    }

    @Override
    public int getPoints(PlayerBoard playerBoard) {
        int points = 0;

        if(playerBoard.isEmpty()){
            return 0;
        }

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 5; column++) {
                try {
                    if (playerBoard.getDie(row, column).getColor().equals(color))
                        points += playerBoard.getDie(row, column).getNumber().getInt();
                } catch (NoDieException e) {
                    //ignore
                }
            }
        }
        return points;
    }
}
