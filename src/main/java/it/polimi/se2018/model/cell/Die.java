package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.NumberEnum;

import java.util.Random;

public class Die {
    private final Color color;
    private NumberEnum number;
    private Random random = new Random();

    public Die(Color color) {
        this.color = color;
        number = NumberEnum.values()[random.nextInt(NumberEnum.values().length)];
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

    public void reRoll() {
        number = NumberEnum.values()[random.nextInt(NumberEnum.values().length)];
    }
}
