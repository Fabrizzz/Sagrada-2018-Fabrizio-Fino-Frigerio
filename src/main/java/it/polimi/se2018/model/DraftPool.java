package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;

import java.util.ArrayList;
import java.util.List;

public class DraftPool {
    private List<Die> dice = new ArrayList<>(9);
    private int numberOfDices;


    public DraftPool(int lenght, DiceBag diceBag) {
        numberOfDices = lenght * 2 + 1;
        for (int i = 0; i < numberOfDices; i++)
            addDie(diceBag.takeDie());
    }

    public void rollDice(DiceBag diceBag) {
        for (int i = 0; i < numberOfDices; i++) {
            dice.add(diceBag.takeDie());
        }
    }

    public void reRollDice() {
        for (Die die : dice) {
            die.reRoll();
        }
    }

    public Die getDie(int i) {
        return dice.get(i);
    }

    public void addDie(Die die) {
        dice.add(die);
    }

    public void removeDie(int i) {
        dice.remove(i);
    }

    public void removeDie(Die die) {
        dice.remove(die);
    }


    public int size() {
        return dice.size();
    }

}
