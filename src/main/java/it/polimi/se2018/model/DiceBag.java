package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.EmptyBagException;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Dice bag
 * @author Giampietro
 */
public class DiceBag implements Serializable {
    private LinkedList<Die> bag = new LinkedList<>();

    /**
     * Constructor
     * @param n Number of die for each color
     */
    public DiceBag(int n) {
        for (int i = 0; i < n; i++) {
            for (Color color : Color.values()) {
                bag.add(new Die(color));
            }
        }
        Collections.shuffle(bag);
    }

    /**
     * Draw a die form the bag
     * @return die draw
     */
    public Die takeDie() {
        if (bag.isEmpty())
            throw new EmptyBagException();
        return bag.poll();
    }

    /**
     * Return the next die to be drawnn
     * @return next die to be drawnn 
     */
    public Die getFirst() {
        return bag.getFirst();
    }

    /**
     * Add a die to the bag
     * @param die die to be added
     */
    public void addDie(Die die) {
        bag.add(die);
        Collections.shuffle(bag);
    }

    /**
     * Return the number of dice remaining in the bag
     * @return number of dice remaining
     */
    public int size() {
        return bag.size();
    }

}
