package it.polimi.se2018.objective_cards.public_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.Arrays;
import java.util.Collections;

/**
 * Obiettivo pubblico 10
 * @author Matteo
 */
public class Card10ColorVariety extends PublicObjective {

    /**
     * Costruttore
     */
    public Card10ColorVariety() {
        super(PublicObjectiveName.VARIETADICOLORE);
    }

    /**
     * Conta il punti: 4 punti per ogni set di dadi di ogni colore
     * @param playerBoard
     * @return punteggio della overboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {
        Integer[] counter = new Integer[5];
        int pos;
        Color color;

        if(playerBoard.isEmpty()){
            return 0;
        }

        Arrays.fill(counter, 0);


        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 5; column++) {
                try {
                    color = playerBoard.getDie(row, column).getColor();
                    pos = Arrays.asList(Color.values()).indexOf(color);
                    counter[pos]++;
                } catch (NoDieException e) {
                    //ignore
                }
            }

        }
        return (4 * Collections.min(Arrays.asList(counter)));
    }
}
