package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;

/**
 * @author Giampietro
 */

public abstract class AbstractRestrictionFactory {

    /**
     * it returns the RestrictionFactory Object
     */
    public static AbstractRestrictionFactory getFactory() {
        return new RestrictionFactory();
    }

    /**
     * It creates a Color Restriction
     */
    public abstract Restriction createColorRestriction(Color color);
    /**
     * It creates a Number Restriction
     */
    public abstract Restriction createNumberRestriction(NumberEnum number);

    /**
     * It creates an Empty Restriction
     */
    public abstract Restriction createNoRestriction();

}
