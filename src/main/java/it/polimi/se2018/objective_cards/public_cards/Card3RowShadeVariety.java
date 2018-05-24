package it.polimi.se2018.objective_cards.public_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.HashSet;

/**
 * Obiettivo pubblico 3
 * @author Matteo
 */
public class Card3RowShadeVariety extends PublicObjective {

    /**
     * Costruttore
     */
    public Card3RowShadeVariety() {
        super(PublicObjectiveName.SFUMATUREDIVERSERIGA);
    }


    /**
     * Conta i punti: ogni riga con tutti i numeri diversi vale 5 punti
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int points = 0;
        boolean noDie = false;
        HashSet<NumberEnum> rowNumber = new HashSet<>();

        if(playerBoard.isEmpty()){
            return 0;
        }

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 5 && !noDie; column++) {
                try {
                    rowNumber.add(playerBoard.getDie(row, column).getNumber());
                } catch (NoDieException e) {
                    noDie = true;
                }
            }
            if (rowNumber.size() == 5) {
                points += 5;
            }
            rowNumber.clear();
            noDie = false;
        }
        return points;
    }

}
