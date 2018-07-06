package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.EmptyBagException;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dice bag
 * @author Giampietro
 */
public class DiceBag implements Serializable {
    private LinkedList<Die> bag = new LinkedList<>();
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private boolean changed = false;

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
        LOGGER.log(Level.FINE,"Dadi creati e mischiati, " + n + " dadi per colore");
    }

    /**
     * Draw a die form the bag
     * @return die draw
     */
    public Die takeDie() throws EmptyBagException{
        if (bag.isEmpty()) {
            throw new EmptyBagException();
        }
        setChanged();
        return bag.poll();
    }

    /**
     * Return the next die to be drawn
     * @return next die to be drawn
     */
    public Die getFirst() throws EmptyBagException{
        if (bag.isEmpty()) {
            throw new EmptyBagException();
        }
        return bag.getFirst();
    }

    /**
     * Add a die to the bag
     * @param die die to be added
     */
    public void addDie(Die die) {
        bag.add(die);
        Collections.shuffle(bag);
        setChanged();
    }

    /**
     * Return the number of dice remaining in the bag
     * @return number of dice remaining
     */
    public int size() {
        return bag.size();
    }

    /**
     * It set Changed to True
     */
    public synchronized void setChanged() {
        changed = true;
    }

    /**
     * It tells if something has changed from the last ModelViewUpdate
     *
     * @return
     */
    public synchronized boolean isChanged() {
        boolean temp = changed;
        changed = false;
        return temp;
    }
}
