package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.enums.NumberEnum;

public class NumberRestriction implements Restriction {
    private final NumberEnum number;


    protected NumberRestriction(NumberEnum number) {
        this.number = number;
    }


    public NumberEnum getNumber() {
        return number;
    }


    @Override
    public boolean isNumberRestriction() {
        return true;
    }

    @Override
    public boolean isColorRestriction() {
        return false;
    }

    @Override
    public boolean noRestriction() {
        return false;
    }

    @Override
    public boolean verifyRestriction(Die die) {
        return die.getNumber().equals(number);

    }
}
