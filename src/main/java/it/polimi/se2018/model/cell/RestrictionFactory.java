package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;

import java.util.EnumMap;

/**
 * A restrictionFactory that uses the flyweight pattern to save memory
 * @author Giampietro
 */

public class RestrictionFactory extends AbstractRestrictionFactory {
    private static EnumMap<Color, ColorRestriction> colorRestrictions = new EnumMap<>(Color.class);
    private static EnumMap<NumberEnum, NumberRestriction> numberRestrictions = new EnumMap<>(NumberEnum.class);
    private static NoRestriction noRestriction = new NoRestriction();

    /**
     * It returns a Color Restriction
     */
    @Override
    public Restriction createColorRestriction(Color color) {
        if (!colorRestrictions.containsKey(color))
            colorRestrictions.put(color, new ColorRestriction(color));

        return colorRestrictions.get(color);
    }

    /**
     * It returns a Number Restriction
     */
    @Override
    public Restriction createNumberRestriction(NumberEnum number) {
        if (!numberRestrictions.containsKey(number))
            numberRestrictions.put(number, new NumberRestriction(number));

        return numberRestrictions.get(number);
    }

    /**
     * It creates an empty Restriction
     */
    @Override
    public Restriction createNoRestriction() {
        return noRestriction;
    }
}
