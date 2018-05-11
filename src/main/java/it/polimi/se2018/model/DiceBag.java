package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;

import java.util.Collections;
import java.util.LinkedList;

public class DiceBag {
    private LinkedList<Die> bag = new LinkedList<>();

    public DiceBag(int n) {
        for (int i = 0; i < n; i++) {
            for (Color color : Color.values()) {
                bag.add(new Die(color));
            }
        }
        Collections.shuffle(bag);
    }

    public Die takeDie() {
        return bag.poll();
    }

    public void addDie(Die die) {
        bag.add(die);
        Collections.shuffle(bag);
    }

    public int size() {
        return bag.size();
    }

}
