package it.polimi.se2018.objective_cards.public_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

/**
 * Obiettivo pubblico 9
 * @author Matteo
 */
public class Card9ColorDiagonals extends PublicObjective {

    /**
     * Costruttore
     */
    public Card9ColorDiagonals() {
        super(PublicObjectiveName.DIAGONALICOLORATE);
    }

    /**
     * Conta il punti: 1 punto per ogni dado che compone una diagonale di dadi con lo stesso colore
     * @param playerBoard
     * @return punteggio della overboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        Color value;
        int point = 0;
        boolean found;

        if(playerBoard.isEmpty()){
            return 0;
        }

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 5; column++) {
                found = false;
                try {
                    value = playerBoard.getDie(row, column).getColor();

                    // Top left
                    if(row != 0 && column != 0){
                        try{
                            if (playerBoard.getDie(row - 1, column - 1).getColor() == value) {
                                found = true;
                                point++;
                            }
                        } catch (NoDieException e){
                            //ignore
                        }
                    }

                    if (!found && row != 3 && column != 0) {
                        try{
                            if (playerBoard.getDie(row + 1, column - 1).getColor() == value) {
                                point++;
                                found = true;
                            }
                        } catch (NoDieException e){
                            //ignore
                        }
                    }

                    if (!found && row != 3 && column != 4) {
                        try{
                            if (playerBoard.getDie(row + 1, column + 1).getColor() == value) {
                                point++;
                                found = true;
                            }
                        } catch (NoDieException e){
                            //ignore
                        }
                    }

                    if (!found && row != 0 && column != 4) {
                        try{
                            if (playerBoard.getDie(row - 1, column + 1).getColor() == value) {
                                point++;
                            }
                        } catch (NoDieException e){
                            //ignore
                        }
                    }
                } catch (NoDieException e) {
                    //ignore
                }
            }
        }

        return point;
    }
}
