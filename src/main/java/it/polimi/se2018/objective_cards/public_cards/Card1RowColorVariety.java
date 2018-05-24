package it.polimi.se2018.objective_cards.public_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.HashSet;

/**
 * Obiettivo pubblico 1
 *
 * @author Matteo
 */
public class Card1RowColorVariety extends PublicObjective {

    /**
     * Costruttore
     */
    protected Card1RowColorVariety() {
        super(PublicObjectiveName.COLORIDIVERSIRIGA);
    }

    /**
     * Conta i punti: ogni riga con tutti i colori diversi vale 6 punti
     *
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int points = 0;
        boolean noDie = false;
        HashSet<Color> rowColor = new HashSet<>();

        if (playerBoard.isEmpty()) {
            return 0;
        }

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 5 && !noDie; column++) {
                try {
                    rowColor.add(playerBoard.getDie(row, column).getColor());
                } catch (NoDieException e) {
                    noDie = true;
                }
            }
            if (rowColor.size() == 5) {
                points += 6;
            }
            rowColor.clear();
            noDie = false;
        }
        return points;
    }

}
