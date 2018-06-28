package it.polimi.se2018.model.cell;

/**
 * An empty Restriction
 * @author Giampietro
 */

public class NoRestriction implements Restriction {

    protected NoRestriction() {
    }

    @Override
    public boolean isNumberRestriction() {
        return false;
    }

    @Override
    public boolean isColorRestriction() {
        return false;
    }


    @Override
    public boolean verifyRestriction(Die die) {
        return true;
    }
}
