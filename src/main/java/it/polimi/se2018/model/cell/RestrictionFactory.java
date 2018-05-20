package it.polimi.se2018.model.cell;

import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;

import java.util.EnumMap;


public class RestrictionFactory extends AbstractRestrictionFactory {  //FlyWeight Pattern per risparmiare memoria, essendo le restriction immutabili

    private static EnumMap<Color, ColorRestriction> colorRestrictions = new EnumMap<>(Color.class);
    private static EnumMap<NumberEnum, NumberRestriction> numberRestrictions = new EnumMap<>(NumberEnum.class);
    private static NoRestriction noRestriction = new NoRestriction();


    @Override
    public Restriction createColorRestriction(Color color) {
        if (!colorRestrictions.containsKey(color))
            colorRestrictions.put(color, new ColorRestriction(color));

        return colorRestrictions.get(color);
    }

    @Override
    public Restriction createNumberRestriction(NumberEnum number) {
        if (!numberRestrictions.containsKey(number))
            numberRestrictions.put(number, new NumberRestriction(number));

        return numberRestrictions.get(number);
    }

    @Override
    public Restriction createNoRestriction() {
        return noRestriction;
    }
}
