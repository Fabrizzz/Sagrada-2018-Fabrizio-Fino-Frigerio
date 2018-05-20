package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.EmptyBagException;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Sacchetto dei dadi
 * @author Giampietro
 */
public class DiceBag implements Serializable {
    private LinkedList<Die> bag = new LinkedList<>();   //Array contenente tutti i dadi del sacchetto

    /**
     * Costruttore
     * @param n Numero dei dadi per ogni colore
     */
    public DiceBag(int n) {
        for (int i = 0; i < n; i++) {
            for (Color color : Color.values()) {    //Per ogni colore viene creato un dado; si ripete questa operazione n volte
                bag.add(new Die(color));
            }
        }
        Collections.shuffle(bag);   //L'array contenente i dadi viene mischiato
    }

    /**
     * Estrae a caso un dado dal sacchetto
     * @return dado estratto
     */
    public Die takeDie() {
        if (bag.isEmpty())
            throw new EmptyBagException();
        return bag.poll();
    }

    /**
     * Ritorna il prossimo dado da estrarre
     *
     * @return dado estratto
     */
    public Die getFirst() {
        return bag.getFirst();
    }

    /**
     * aggiunge un dado al sacchetto
     * @param die dado da aggiungere
     */
    public void addDie(Die die) {
        bag.add(die);
        Collections.shuffle(bag);
    }

    /**
     * Restituisce numero dei dadi presenti nel sacchetto
     * @return numero dei dadi nel sacchetto
     */
    public int size() {
        return bag.size();
    }

}
