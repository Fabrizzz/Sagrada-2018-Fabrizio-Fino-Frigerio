package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.NumberEnum;

public abstract class AbstractRestrictionFactory {

    public static AbstractRestrictionFactory getFactory() {
        return new RestrictionFactory();
    }

    public abstract Restriction createColorRestriction(Color color);

    public abstract Restriction createNumberRestriction(NumberEnum number);

    public abstract Restriction createNoRestriction();

}