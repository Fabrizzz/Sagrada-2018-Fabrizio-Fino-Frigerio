package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;

/**
 * Obiettivo pubblico 10
 * @author Matteo
 */
public class Card10VarietaDiColori extends PublicObjective {

    /**
     * Costruttore
     * @param objectiveName
     */
    public Card10VarietaDiColori(PublicObjectiveName objectiveName) {
        super(objectiveName);
    }

    /**
     * Conta il punti: 4 punti per ogni set di dadi di ogni colore
     * @param playerBoard
     * @return punteggio della overboard
     */
    @Override
    public int getPoints(PlayerBoard playerBoard) {
        int row, column;
        int[] color = new int[5];   // 0 red, 1 yellow, 2 green, 3 blue, 4 purple
        Color value;
        int min;

        if(playerBoard.isEmpty()){
            return 0;
        }

        for (int i = 0; i < 5; i++) {
            color[i] = 0;
        }

        for(row = 0; row < 4; row++){
            for(column = 0; column < 5; column++){
                try {
                    value = playerBoard.getDie(row,column).getColor();
                    if(value == Color.RED){
                        color[0]++;
                    }
                    if(value == Color.YELLOW){
                        color[1]++;
                    }
                    if(value == Color.GREEN){
                        color[2]++;
                    }
                    if(value == Color.BLUE){
                        color[3]++;
                    }
                    if(value == Color.PURPLE){
                        color[4]++;
                    }
                } catch (NoDieException e) { }
            }

        }
        min = minArray(color);
        return (4*color[min]);
    }

    /**
     * Metodo che calcola e ritorna l'indice del valore minimo di un array di interi
     * @param array
     * @return indice del valore min dell'array
     */
    public int minArray(int[] array){

        int min = 0;

        for (int i = 0; i < 6; i++){
            if(array[i] < array[min]){
                min = i;
            }
        }
        return min;
    }
}
