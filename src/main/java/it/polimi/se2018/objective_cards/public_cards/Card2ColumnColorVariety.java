package it.polimi.se2018.objective_cards.public_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.HashSet;

/**
 * Obiettivo pubblico 2
 * @author Matteo
 */
public class Card2ColumnColorVariety extends PublicObjective {

    /**
     * Costruttore
     */
    public Card2ColumnColorVariety() {
        super(PublicObjectiveName.COLORIDIVERSICOLONNA);
    }

    /**
     * Conta i punti: ogni colonna con tutti i colori diversi vale 5 punti
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int points = 0;
        boolean noDie = false;
        HashSet<Color> columnColor = new HashSet<>();

        if(playerBoard.isEmpty()){
            return 0;
        }

        for (int column = 0; column < 5; column++) {
            for (int row = 0; row < 4 && !noDie; row++) {
                try {
                    columnColor.add(playerBoard.getDie(row,column).getColor());
                } catch (NoDieException e) {
                    noDie = true;
                }
            }
            if (columnColor.size() == 4) {
                points += 5;
            }
            columnColor.clear();
            noDie = false;
        }
        return points;
    }
}
