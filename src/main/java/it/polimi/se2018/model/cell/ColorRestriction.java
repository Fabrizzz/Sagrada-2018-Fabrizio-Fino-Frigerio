package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.enums.Color;

import java.io.Serializable;

public class ColorRestriction implements Restriction, Serializable {
    private final Color color;

    protected ColorRestriction(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean isNumberRestriction() {
        return false;
    }

    @Override
    public boolean isColorRestriction() {
        return true;
    }

    @Override
    public boolean noRestriction() {
        return false;
    }

    @Override
    public boolean verifyRestriction(Die die) {
        return die.getColor().equals(color);

    }

}
