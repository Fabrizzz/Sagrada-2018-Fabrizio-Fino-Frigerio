package it.polimi.se2018.model.cell;

public class Cell {
    private final Restriction restriction;
    private Die die;
    private boolean isUsed = false;

    public Cell(Restriction restriction) {
        this.restriction = restriction;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void removeDie() {
        isUsed = false;
    }

    public Die getDie() {
        return die;
    }

    public void setDie(Die die) {
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
