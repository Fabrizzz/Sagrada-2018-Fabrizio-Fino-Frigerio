package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.ArrayList;

/**
 * Obiettivo pubblico 3
 * @author Matteo
 */
public class Card3SfumatureDiverseRiga extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card3SfumatureDiverseRiga(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }


    /**
     * Conta i punti: ogni riga con tutti i numeri diversi vale 5 punti
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, column;
        int point = 0;
        boolean noDie;
        ArrayList<Integer> rowNum = new ArrayList<Integer>();

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(row = 0; row < 4; row++){
            noDie = false;
            for(column = 0; column < 5; column++){
                try {
                    rowNum.add(playerBoard.getDie(row,column).getNumber().getInt());
                } catch (NoDieException e) {
                    noDie = true;
                    column = 5;
                }
            }
            if(!noDie && controlNumRow(rowNum)){
                point = point + 5;
            }
            removeAll(rowNum);
        }
        return point;
    }

    /**
     * Controlla se su una riga ci sono tutti numeri diversi
     * @param rowNum
     * @return true se su una riga ci sono tutti numeri diversi, false altrimenti
     */
    public boolean controlNumRow(ArrayList<Integer> rowNum){

        int i;
        int j;

        j = 1;
        for (i = 0; i < rowNum.size(); i++){
            while (j < rowNum.size()){
                if(rowNum.get(i) == rowNum.get(j)){
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
     * @param rowNum
     */
    public void removeAll (ArrayList<Integer> rowNum){

        while (rowNum.size() != 0){
            rowNum.remove(0);
        }
    }
}
