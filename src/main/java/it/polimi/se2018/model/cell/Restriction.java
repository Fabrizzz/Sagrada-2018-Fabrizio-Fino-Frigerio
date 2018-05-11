package it.polimi.se2018.model.cell;

public interface Restriction {

    boolean isNumberRestriction();

    boolean isColorRestriction();

    boolean noRestriction();

    boolean verifyRestriction(Die die);

}
