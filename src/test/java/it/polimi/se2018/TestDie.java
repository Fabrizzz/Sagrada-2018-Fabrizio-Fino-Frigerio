package it.polimi.se2018;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Giampietro
 */
public class TestDie {

    /**
     * Test getting the color of a die
     */
    @Test
    public void testGetColor() {
        for (Color color : Color.values()) {
            Die die = new Die(color);
            assertEquals(color, die.getColor());
        }
    }

    /**
     * test getting the number of a die
     */
    @Test
    public void testGetNumber() {
        for(int i = 0; i<100; i++){
            Die die = new Die(Color.BLUE);
            assertTrue(Arrays.asList(NumberEnum.values()).contains(die.getNumber()));
        }

    }

    /**
     * test setting the number of a die
     */
    @Test
    public void testSetNumber() {
        Die die = new Die(Color.BLUE);
        for (NumberEnum number : NumberEnum.values()) {
            die.setNumber(number);
            assertEquals(number, die.getNumber());
        }
    }

    /**
     * test rerolloing a die
     */
    @Test
    public void testReRoll() {
        Die die = new Die(Color.BLUE);
        for(int i = 0; i<100; i++){
            die.reRoll();
            assertTrue(Arrays.asList(NumberEnum.values()).contains(die.getNumber()));
        }
    }

    /**
     * test flipping a die
     */
    @Test
    public void testFlip(){
        for (int i = 0; i < 100; i++) {
            Die die = new Die(Color.BLUE);
            int number = die.getNumber().getInt();
            die.flip();
            assertEquals(die.getNumber().getInt(), 7 - number);
        }
    }

}