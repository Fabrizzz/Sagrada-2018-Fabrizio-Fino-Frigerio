package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;

import java.io.Serializable;
import java.util.Random;

/**
 * Classe dado
 * @author Giampietro
 */
public class Die implements Serializable {
    private final Color color;  //Colore del dado; Color è un enum contenente i colori corretti
    private NumberEnum number;  //Numero del dado; NumberEnum è un enum contenente i numeri 1 a 6
    private Random random = new Random();

    /**
     * Costruttore
     * @param color colore del dado
     */
    public Die(Color color) {   //Viene passato il colore al costruttore perchè un dado viene creato dalla classe DiceBag che controlla il numero esatto di dati (18 per colore)
        this.color = color; //Inizializzazione del colore passato
        number = NumberEnum.values()[random.nextInt(NumberEnum.values().length)];   //Metodo della classe Random per generare un numero casuale; NumberEnum.values().length = 6, quindi si genera un valore tra 1 a 6
    }

    /**
     * Ritorna il colore del dado
     * @return colore del dado
     */
    public Color getColor() {
        return color;
    }

    /**
     * Ritorna il numero del dado
     * @return numero del dado
     */
    public NumberEnum getNumber() {
        return number;
    }

    /**
     * Imposta il numero del dado
     * @param number numero del dado
     */
    public void setNumber(NumberEnum number) {
        this.number = number;
    }

    /**
     * Ritira il dado rigenerando il valore casualmente
     */
    public void reRoll() {
        number = NumberEnum.values()[random.nextInt(NumberEnum.values().length)];
    }

    /**
     * Gira il dado sottosopra modificando il valore del dado
     */
    public void flip() {
        setNumber(NumberEnum.getNumber(7 - getNumber().getInt()));
    }
}
