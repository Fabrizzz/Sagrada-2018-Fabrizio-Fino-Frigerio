package it.polimi.se2018;

import it.polimi.se2018.model.cell.AbstractRestrictionFactory;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.Restriction;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestRestriction {

    private Die die;
    private Restriction restriction;
    private AbstractRestrictionFactory factory = AbstractRestrictionFactory.getFactory();


    @Test
    public void testTrueRestriction() {

        die = new Die(Color.BLUE);
        restriction = factory.createNoRestriction();
        assertTrue(restriction.verifyRestriction(die));

        for (Color color : Color.values()) {
            die = new Die(color);
            restriction = factory.createColorRestriction(color);
            assertTrue(restriction.isColorRestriction());
            assertTrue(restriction.verifyRestriction(die));
        }

        for (NumberEnum number : NumberEnum.values()) {
            die = new Die(Color.BLUE);
            restriction = factory.createNumberRestriction(number);
            assertTrue(restriction.isNumberRestriction());
            if (number.equals(die.getNumber()))
                assertTrue(restriction.verifyRestriction(die));
            else
                assertFalse(restriction.verifyRestriction(die));
        }

    }

    @Test
    public void testFalseRestriction() {
        restriction = factory.createNoRestriction();
        assertFalse(restriction.isColorRestriction());
        assertFalse(restriction.isNumberRestriction());

        for (Color color : Color.values()) {
            restriction = factory.createColorRestriction(color);
            assertFalse(restriction.isNumberRestriction());
            for (Color colors : Color.values()) {
                die = new Die(colors);
                if (!color.equals(colors))
                    assertFalse(restriction.verifyRestriction(die));
            }
        }

        for (NumberEnum number : NumberEnum.values()) {
            restriction = factory.createNumberRestriction(number);
            assertFalse(restriction.isColorRestriction());

        }

    }


}
