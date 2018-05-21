package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.ArrayList;

/**
 * Obbiettivo pubblico 2
 * @author Matteo
 */
public class Card2ColoriDiversiColonna extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card2ColoriDiversiColonna(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta i punti: ogni colonna con tutti i colori diversi vale 6 punti
     * @param playerBoard
     * @return punteggio della playerBoard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, colomn;
        int point = 0;
        ArrayList<Color> colomnColor = new ArrayList<Color>();

        if(playerBoard.isEmpty()){
            return 0;
        }

        for(colomn = 0; colomn < 5; colomn++){
            for(row = 0; row < 4; row++){
                //if(playerBoard.containsDie(row,colomn)){
                try {
                    colomnColor.add(playerBoard.getDie(row,colomn).getColor());
                } catch (NoDieException e) {
                    e.printStackTrace();
                }
            }
            if(controlColorColomn(colomnColor)){
                point = point + 6;
            }
            removeAll(colomnColor);
        }
        return point;
    }

    /**
     * Controlla se su una colonna ci sono tutti colori diversi
     * @param colomnColor
     * @return true se su una colonna ci sono tutti colori diversi, false altrimenti
     */
    public boolean controlColorColomn(ArrayList<Color> colomnColor){

        int i;
        int j;

        j = 1;
        for (i = 0; i < colomnColor.size(); i++){
            while (j < colomnColor.size()){
                if(colomnColor.get(i) == colomnColor.get(j)){
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
     * @param colomnColor
     */
    public void removeAll (ArrayList<Color> colomnColor){

        while (colomnColor.size() != 0){
            colomnColor.remove(0);
        }
    }
}
