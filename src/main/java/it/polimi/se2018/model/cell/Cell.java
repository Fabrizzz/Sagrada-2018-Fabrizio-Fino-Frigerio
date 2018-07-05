package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.exceptions.AlreadySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;

/**
 * Cells in the player board
 * @author Giampietro
 */
public class Cell implements Serializable {
    private final Restriction restriction;
    private Die die;
    private boolean isUsed = false;

    /**
     * Constructor
     * @param restriction restriction rule to be applied to the cell
     */
    public Cell(Restriction restriction) {
        this.restriction = restriction;
    }

    /**
     * Return if the cell contains a die.
     * @return Boolean If the cell contains a dia.
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Remove the die from the cell
     * @throws NoDieException if the cell is empty
     */
    public void removeDie() throws NoDieException {

        if (!isUsed())
            throw new NoDieException();

        isUsed = false;
    }

    /**
     * Return the die contained in the cell
     * @return the die 
     * @throws NoDieException if the cell is empty
     */
    public Die getDie() throws NoDieException {
        if (!isUsed())
            throw new NoDieException();
        return die;
    }

    /**
     * Insert a die in the cell
     * @param die die to insert
     * @throws AlreadySetDie if the cell already contains a die
     */
    public void setDie(Die die) throws AlreadySetDie {
        if (isUsed)
            throw new AlreadySetDie();
        this.die = die;
        isUsed = true;
    }

    /**
     * Check if the die respect the cell restriction
     * @param die die to use in the verification
     * @return return if the die respect the restriction
     */
    public boolean verifyRestriction(Die die) {
        return restriction.verifyRestriction(die);
    }

    /**
     * Return the restriction applied to the cell
     * @return the restriction
     */
    public Restriction getRestriction() {
        return restriction;
    }


}
