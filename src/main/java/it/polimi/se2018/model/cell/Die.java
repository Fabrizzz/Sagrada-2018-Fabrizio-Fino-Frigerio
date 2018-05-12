package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.NumberEnum;
import java.util.Random;

//Costruttore
public class Die {
    private final Color color;  //Colore del dado; Color è un enum contenente i colori corretti
    private NumberEnum number;  //Numero del dado; NumberEnum è un enum contenente i numeri 1 a 6
    private Random random = new Random();

    //Costruttore
    public Die(Color color) {   //Viene passato il colore al costruttore perchè un dado viene creato dalla classe DiceBag che controlla il numero esatto di dati (18 per colore)
        this.color = color; //Inizializzazione del colore passato
        number = NumberEnum.values()[random.nextInt(NumberEnum.values().length)];   //Metodo della classe Random per generare un numero casuale; NumberEnum.values().length = 6, quindi si genera un valore tra 1 a 6
    }

    public Color getColor() {
        return color;
    }

    public NumberEnum getNumber() {
        return number;
    }

    public void setNumber(NumberEnum number) {
        this.number = number;
    }

    //Generazione di un nuovo numero casuale tra 1 e 6
    public void reRoll() {
        number = NumberEnum.values()[random.nextInt(NumberEnum.values().length)];
    }
}
