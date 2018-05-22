package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.exceptions.NoDieException;

/**
 * Obiettivo pubblico 7
 * @author Matteo
 */
public class Card7SfumatureScure extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card7SfumatureScure(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta i punti: 2 punti per ogni coppia di 5 e 6
     * @param playerBoard
     * @return punteggio della playboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, column;
        int five = 0;
        int six = 0;
        Die die;

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(row = 0; row < 4; row++){
            for(column = 0; column < 5; column++){
                try {
                    die = playerBoard.getDie(row,column);
                    if(die.getNumber().getInt() == 5){
                        five++;
                    }
                    if(die.getNumber().getInt() == 6){
                        six++;
                    }
                } catch (NoDieException e) { }
            }

        }
        if(five < six){
            return (2*five);
        }
        else {
            return (2*six);
        }
    }
}
