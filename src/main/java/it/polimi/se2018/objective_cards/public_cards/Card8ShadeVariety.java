package it.polimi.se2018.objective_cards.public_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.Arrays;
import java.util.Collections;

/**
 * Obiettivo pubblico 8
 * @author Matteo
 */
public class Card8ShadeVariety extends PublicObjective {

    /**
     * Costruttore
     */
    protected Card8ShadeVariety() {
        super(PublicObjectiveName.SFUMATUREDIVERSE);
    }

    /**
     * Conta i punti: 5 punti per ogni set di dadi di ogni valore
     * @param playerBoard
     * @return punteggio della playboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, column;
        Integer[] counter = new Integer[6];
        int pos;

        if(playerBoard.isEmpty()){
            return 0;
        }

        Arrays.fill(counter, 0);


        for(row = 0; row < 4; row++){
            for(column = 0; column < 5; column++){
                try {
                    pos = playerBoard.getDie(row, column).getNumber().getInt() - 1;
                    counter[pos]++;
                } catch (NoDieException e) {
                    //ignore
                }
            }

        }
        return (5 * Collections.min(Arrays.asList(counter)));
    }
}
