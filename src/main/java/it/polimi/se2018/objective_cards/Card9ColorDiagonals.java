package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

/**
 * Obiettivo pubblico 9
 * @author Matteo
 */
public class Card9ColorDiagonals extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card9ColorDiagonals(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta il punti: 1 punto per ogni dado che compone una diagonale di dadi con lo stesso colore
     * @param playerBoard
     * @return punteggio della overboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {
        int row, column;
        int[] color = new int[5];   // 0 red, 1 yellow, 2 green, 3 blue, 4 purple
        Color value1, value2;
        int point = 0;
        boolean noDie, bool;

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(row = 0; row < 4; row++){
            for(column = 0; column < 5; column++){
                noDie = false;
                bool = true;
                value2 = null;
                try {
                    value1 = playerBoard.getDie(row,column).getColor();

                    // Top left
                    if(row != 0 && column != 0){
                        try{
                            value2 = playerBoard.getDie(row - 1, column - 1).getColor();
                        } catch (NoDieException e){
                            noDie = true;
                        }
                        if(!noDie && value1 == value2){
                            point++;
                            bool = false;
                        }
                    }

                    //Bottom left
                    noDie = false;
                    if(bool && row != 3 && column != 0){
                        try{
                            value2 = playerBoard.getDie(row + 1, column - 1).getColor();
                        } catch (NoDieException e){
                            noDie = true;
                        }
                        if(!noDie && value1 == value2){
                            point++;
                            bool = false;
                        }
                    }

                    //Bottom right
                    noDie = false;
                    if(bool && row != 3 && column != 4){
                        try{
                            value2 = playerBoard.getDie(row + 1, column + 1).getColor();
                        } catch (NoDieException e){
                            noDie = true;
                        }
                        if(!noDie && value1 == value2){
                            point++;
                            bool = false;
                        }
                    }

                    //Top right
                    noDie = false;
                    if(bool && row != 0 && column != 4){
                        try{
                            value2 = playerBoard.getDie(row - 1, column + 1).getColor();
                        } catch (NoDieException e){
                            noDie = true;
                        }
                        if(!noDie && value1 == value2){
                            point++;
                        }
                    }
                } catch (NoDieException e) { }
            }
        }

        return point;
    }
}
