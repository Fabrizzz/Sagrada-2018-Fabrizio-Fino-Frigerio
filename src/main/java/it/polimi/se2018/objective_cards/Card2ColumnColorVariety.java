package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.ArrayList;

/**
 * Obiettivo pubblico 2
 * @author Matteo
 */
public class Card2ColumnColorVariety extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card2ColumnColorVariety(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta i punti: ogni colonna con tutti i colori diversi vale 5 punti
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, column;
        int point = 0;
        boolean noDie;
        ArrayList<Color> columnColor = new ArrayList<Color>();

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(column = 0; column < 5; column++){
            noDie = false;
            for(row = 0; row < 4; row++){
                try {
                    columnColor.add(playerBoard.getDie(row,column).getColor());
                } catch (NoDieException e) {
                    noDie = true;
                    row = 4;
                }
            }
            if(!noDie && controlColorColumn(columnColor)){
                point = point + 5;
            }
            removeAll(columnColor);
        }
        return point;
    }

    /**
     * Controlla se su una colonna ci sono tutti colori diversi
     * @param columnColor
     * @return true se su una colonna ci sono tutti colori diversi, false altrimenti
     */
    public boolean controlColorColumn(ArrayList<Color> columnColor){

        int i;
        int j;

        if(columnColor.size() == 0){
            return false;
        }

        j = 1;
        for (i = 0; i < columnColor.size(); i++){
            while (j < columnColor.size()){
                if(columnColor.get(i) == columnColor.get(j)){
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
     * @param columnColor
     */
    public void removeAll (ArrayList<Color> columnColor){

        while (columnColor.size() != 0){
            columnColor.remove(0);
        }
    }
}
