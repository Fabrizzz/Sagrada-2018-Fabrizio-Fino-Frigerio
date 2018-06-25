package it.polimi.se2018.model.cell;

import java.io.Serializable;

public interface Restriction extends Serializable {

    boolean isNumberRestriction();

    boolean isColorRestriction();

    boolean verifyRestriction(Die die);

}
