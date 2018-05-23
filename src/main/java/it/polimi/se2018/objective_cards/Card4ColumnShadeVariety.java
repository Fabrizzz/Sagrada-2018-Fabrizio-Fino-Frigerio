package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.ArrayList;

/**
 * Obiettivo pubblico 4
 * @author Matteo
 */
public class Card4ColumnShadeVariety extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card4ColumnShadeVariety(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta i punti: ogni colonna con tutti i numeri diversi vale 4 punti
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, column;
        int point = 0;
        boolean noDie;
        ArrayList<Integer> columnNum = new ArrayList<Integer>();

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(column = 0; column < 5; column++){
            noDie = false;
            for(row = 0; row < 4; row++){
                try {
                    columnNum.add(playerBoard.getDie(row,column).getNumber().getInt());
                } catch (NoDieException e) {
                    noDie = true;
                    row = 4;
                }
            }
            if(!noDie && controlNumColumn(columnNum)){
                point = point + 4;
            }
            removeAll(columnNum);
        }
        return point;
    }

    /**
     * Controlla se su una colonna ci sono tutti numeri diversi
     * @param columnNum
     * @return true se su una colonna ci sono tutti numeri diversi, false altrimenti
     */
    public boolean controlNumColumn(ArrayList<Integer> columnNum){

        int i;
        int j;

        j = 1;
        for (i = 0; i < columnNum.size(); i++){
            while (j < columnNum.size()){
                if(columnNum.get(i) == columnNum.get(j)){
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
     * @param columnNum
     */
    public void removeAll (ArrayList<Integer> columnNum){

        while (columnNum.size() != 0){
            columnNum.remove(0);
        }
    }
}
