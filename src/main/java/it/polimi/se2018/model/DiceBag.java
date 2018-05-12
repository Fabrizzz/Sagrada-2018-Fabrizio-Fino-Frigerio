package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;

import java.util.Collections;
import java.util.LinkedList;

public class DiceBag {
    private LinkedList<Die> bag = new LinkedList<>();   //Array contenente tutti i dadi del sacchetto

    //Costruttore
    public DiceBag(int n) { //Viene passato il numero di dadi per ogni colore (quindi 18 in questo caso)
        for (int i = 0; i < n; i++) {
            for (Color color : Color.values()) {    //Per ogni colore viene creato un dado; si ripete questa operazione n volte
                bag.add(new Die(color));
            }
        }
        Collections.shuffle(bag);   //L'array contenente i dadi viene mischiato
    }

    public Die takeDie() {
        return bag.poll();  //Estrazione di un dado a caso
    }

    //Viene aggiunto un dado al sacchetto
    public void addDie(Die die) {
        bag.add(die);
        Collections.shuffle(bag);
    }

    //Ritorna in numero di dadi nel sacchetto
    public int size() {
        return bag.size();
    }

}
