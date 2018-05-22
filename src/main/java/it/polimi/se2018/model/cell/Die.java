package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;

import java.io.Serializable;
import java.util.Random;

/**
 * Die
 * @author Giampietro
 */
public class Die implements Serializable {
    private final Color color;
    private NumberEnum number;
    private Random random = new Random();

    /**
     * Constructor
     * @param color color of the die
     */
    public Die(Color color) {  
        this.color = color;
        number = NumberEnum.values()[random.nextInt(NumberEnum.values().length)]; 
    }

    /**
     * Return the die color
     * @return color of the die
     */
    public Color getColor() {
        return color;
    }

    /**
     * Return the number on the die
     * @return value of the die
     */
    public NumberEnum getNumber() {
        return number;
    }

    /**
     * Set the die value
     * @param number new value of the die
     */
    public void setNumber(NumberEnum number) {
        this.number = number;
    }

    /**
     * Roll the die
     */
    public void reRoll() {
        number = NumberEnum.values()[random.nextInt(NumberEnum.values().length)];
    }

    /**
     * Turn the die upside down and change the die value
     */
    public void flip() {
        setNumber(NumberEnum.getNumber(7 - getNumber().getInt()));
    }
}
