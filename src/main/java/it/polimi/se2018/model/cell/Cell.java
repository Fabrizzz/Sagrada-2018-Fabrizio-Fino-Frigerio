package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;

public class Cell implements Serializable {
    private final Restriction restriction;
    private Die die;
    private boolean isUsed = false;

    public Cell(Restriction restriction) {
        this.restriction = restriction;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void removeDie() throws NoDieException {

        if (!isUsed())
            throw new NoDieException();

        isUsed = false;
    }

    public Die getDie() throws NoDieException {
        if (!isUsed())
            throw new NoDieException();
        return die;
    }

    public void setDie(Die die) throws AlredySetDie {
        if (isUsed)
            throw new AlredySetDie();
        this.die = die;
        isUsed = true;
    }

    public boolean verifyRestriction(Die die) {
        return restriction.verifyRestriction(die);
    }

    public Restriction getRestriction() {
        return restriction;
    }


}
