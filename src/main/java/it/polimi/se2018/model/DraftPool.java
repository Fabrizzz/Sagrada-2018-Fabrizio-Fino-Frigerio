package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Draft pool
 * @author Giampietro
 */
public class DraftPool implements Serializable {

    private List<Die> dice = new ArrayList<>(9);    //Lista contenente tutti i dadi pescati
    private int numberOfDices;  //Numero di dadi da pescare a ogni round

    /**
     * Constructor
     * @param lenght Number of turns
     * @param diceBag dice bag
     */
    public DraftPool(int lenght, DiceBag diceBag) {
        numberOfDices = lenght * 2 + 1; //Inizializzazione del numero di dadi da pescare ogni round
        for (int i = 0; i < numberOfDices; i++)
            addDie(diceBag.takeDie());  //Si pescano i dadi dal sacchetto
    }

    /**
     * Dice are drawn from the bag and added to the draftpool
     * @param diceBag dice bag
     */
    public void rollDice(DiceBag diceBag) {
        for (int i = 0; i < numberOfDices; i++) {
            dice.add(diceBag.takeDie());
        }
    }

    /**
     * Roll again all the dice in the draftpool
     */
    public void reRollDice() {
        for (Die die : dice) {
            die.reRoll();
        }
    }

    /**
     * Return the die in the draftpool in position i
     * @param i position of the die in the draftpool
     * @return die extracted
     * @throws NoDieException if no die is found in the draftpool in positio i
     */
    public Die getDie(int i) throws NoDieException {
        if (i >= size())
            throw new NoDieException();
        return dice.get(i);
    }

    /**
     * Add a die to the draftpool
     * @param die die to be added
     */
    public void addDie(Die die) {
        dice.add(die);
    }

    /**
     * Remove the die in position i from the draftpool
     * @param i position of the die to be removed
     */
    public void removeDie(int i) {
        dice.remove(i);
    }

    /**
     * Remove the die die from the draftpool
     * @param die die to be removed
     * @throws NoDieException if the die is not contained in the draftpool
     */
    public void removeDie(Die die) throws NoDieException {
        if (!dice.contains(die))
            throw new NoDieException();
        dice.remove(die);
    }

    /**
     * Remove all the dice in the draftpool
     * @return return a copy of the draftpool dice removed
     */
    public List<Die> removeAll() {
        List<Die> temp = new ArrayList<>(dice);
        dice.clear();
        return temp;

    }

    /**
     * Return the size of the draftpool
     * @return the size of the draftpool
     */
    public int size() {
        return dice.size();
    }

}
