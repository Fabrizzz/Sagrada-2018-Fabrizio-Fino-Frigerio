package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.exceptions.NoDieException;

/**
 * Obiettivo pubblico 6
 * @author Matteo
 */
public class Card6SfumatureMedie extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card6SfumatureMedie(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta i punti: 2 punti per ogni coppia di 3 e 4
     * @param playerBoard
     * @return punteggio della playboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, column;
        int three = 0;
        int four = 0;
        Die die;

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(row = 0; row < 4; row++){
            for(column = 0; column < 5; column++){
                try {
                    die = playerBoard.getDie(row,column);
                    if(die.getNumber().getInt() == 3){
                        three++;
                    }
                    if(die.getNumber().getInt() == 4){
                        four++;
                    }
                } catch (NoDieException e) { }
            }

        }
        if(three < four){
            return (2*three);
        }
        else {
            return (2*four);
        }
    }
}
