package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.exceptions.NoDieException;

/**
 * Obiettivo pubblico 8
 * @author Matteo
 */
public class Card8ShadeVariety extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card8ShadeVariety(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta i punti: 5 punti per ogni set di dadi di ogni valore
     * @param playerBoard
     * @return punteggio della playboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {

        int row, column;
        int[] counter = new int[6];
        int value;
        int min;

        if(playerBoard.isEmpty()){
            return 0;
        }

        for (int i = 0; i < 6; i++) {
            counter[i] = 0;
        }

        for(row = 0; row < 4; row++){
            for(column = 0; column < 5; column++){
                try {
                    value = playerBoard.getDie(row,column).getNumber().getInt();
                    value--;
                    counter[value]++;
                } catch (NoDieException e) { }
            }

        }
        min = minArray(counter);
        return (5*counter[min]);
    }

    /**
     * Metodo che calcola e ritorna l'indice del valore minimo di un array di interi
     * @param counter
     * @return indice del valore min dell'array
     */
    public int minArray(int[] counter){

        int min = 0;

        for (int i = 0; i < 6; i++){
            if(counter[i] < counter[min]){
                min = i;
            }
        }
        return min;
    }
}
