package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.ArrayList;

/**
 * Obiettivo pubblico 4
 * @author Matteo
 */
public class Card4SfumatureDiverseColonne extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card4SfumatureDiverseColonne(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta i punti: ogni colonna con tutti i numeri diversi vale 4 punti
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, colomn;
        int point = 0;
        boolean noDie;
        ArrayList<Integer> colomnNum = new ArrayList<Integer>();

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(colomn = 0; colomn < 5; colomn++){
            noDie = false;
            for(row = 0; row < 4; row++){
                try {
                    colomnNum.add(playerBoard.getDie(row,colomn).getNumber().getInt());
                } catch (NoDieException e) {
                    noDie = true;
                    row = 4;
                }
            }
            if(!noDie && controlNumColomn(colomnNum)){
                point = point + 4;
            }
            removeAll(colomnNum);
        }
        return point;
    }

    /**
     * Controlla se su una colonna ci sono tutti numeri diversi
     * @param colomnNum
     * @return true se su una colonna ci sono tutti numeri diversi, false altrimenti
     */
    public boolean controlNumColomn(ArrayList<Integer> colomnNum){

        int i;
        int j;

        j = 1;
        for (i = 0; i < colomnNum.size(); i++){
            while (j < colomnNum.size()){
                if(colomnNum.get(i) == colomnNum.get(j)){
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
     * @param colomnNum
     */
    public void removeAll (ArrayList<Integer> colomnNum){

        while (colomnNum.size() != 0){
            colomnNum.remove(0);
        }
    }
}
