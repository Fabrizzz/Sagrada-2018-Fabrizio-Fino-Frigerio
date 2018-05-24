package it.polimi.se2018.objective_cards.public_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.HashSet;

/**
 * Obiettivo pubblico 4
 * @author Matteo
 */
public class Card4ColumnShadeVariety extends PublicObjective {

    /**
     * Costruttore
     */
    protected Card4ColumnShadeVariety() {
        super(PublicObjectiveName.SFUMATUREDIVERSECOLONNA);
    }

    /**
     * Conta i punti: ogni colonna con tutti i numeri diversi vale 4 punti
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int points = 0;
        boolean noDie = false;
        HashSet<NumberEnum> columnNumber = new HashSet<>();

        if(playerBoard.isEmpty()){
            return 0;
        }

        for (int column = 0; column < 5; column++) {
            for (int row = 0; row < 4 && !noDie; row++) {
                try {
                    columnNumber.add(playerBoard.getDie(row, column).getNumber());
                } catch (NoDieException e) {
                    noDie = true;
                }
            }
            if (columnNumber.size() == 4) {
                points += 4;
            }
            columnNumber.clear();
            noDie = false;
        }
        return points;
    }

}
