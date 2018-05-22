package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.exceptions.NoDieException;


/**
 * Obiettivo pubblico 5
 * @author Matteo
 */
public class Card5SfumatureChiare extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card5SfumatureChiare(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta i punti: 2 punti per ogni coppia di 1 e 2
     * @param playerBoard
     * @return punteggio della playboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, column;
        int one = 0;
        int two = 0;
        Die die;

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(row = 0; row < 4; row++){
            for(column = 0; column < 5; column++){
                try {
                    die = playerBoard.getDie(row,column);
                    if(die.getNumber().getInt() == 1){
                        one++;
                    }
                    if(die.getNumber().getInt() == 2){
                        two++;
                    }
                } catch (NoDieException e) { }
            }

        }
        if(one < two){
            return (2*one);
        }
        else {
            return (2*two);
        }
    }
}
