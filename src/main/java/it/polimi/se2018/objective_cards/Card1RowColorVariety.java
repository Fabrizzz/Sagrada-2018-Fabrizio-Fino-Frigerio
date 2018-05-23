package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.ArrayList;

/**
 * Obiettivo pubblico 1
 * @author Matteo
 */
public class Card1RowColorVariety extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card1RowColorVariety(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta i punti: ogni riga con tutti i colori diversi vale 6 punti
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, column;
        int point = 0;
        boolean noDie;
        ArrayList<Color> rowColor = new ArrayList<Color>();

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(row = 0; row < 4; row++){
            noDie = false;
            for(column = 0; column < 5; column++){
                try {
                    rowColor.add(playerBoard.getDie(row,column).getColor());
                } catch (NoDieException e) {
                    noDie = true;
                    column = 5;
                }
            }
            if(!noDie && controlColorRow(rowColor)){
                point = point + 6;
            }
            removeAll(rowColor);
        }
        return point;
    }

    /**
     * Controlla se su una riga ci sono tutti colori diversi
     * @param rowColor
     * @return true se su una riga ci sono tutti colori diversi, false altrimenti
     */
    public boolean controlColorRow(ArrayList<Color> rowColor){

        int i;
        int j;

        if(rowColor.size() == 0){
            return false;
        }

        j = 1;
        for (i = 0; i < rowColor.size(); i++){
            while (j < rowColor.size()){
                if(rowColor.get(i) == rowColor.get(j)){
                    return false;
                }
                j++;
            }
            j = i + 2;
        }
        return true;
    }

    /**
     * Svuota un arraylist
     * @param rowColor
     */
    public void removeAll (ArrayList<Color> rowColor){

        while (rowColor.size() != 0){
            rowColor.remove(0);
        }
    }
}
