package it.polimi.se2018;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.NumberEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DieTest {

    @Test
    public void getColor() {
        Die dice = new Die(Color.BLUE);
        assertEquals(Color.BLUE, dice.getColor());
    }

    @Test
    public void getNumber() {
        boolean result;
        Die dice = new Die(Color.BLUE);
        for(int i = 0; i<100; i++){
            result = dice.getNumber().getInt() <= NumberEnum.SIX.getInt() && dice.getNumber().getInt() >= NumberEnum.ONE.getInt();
            assertTrue(result);
        }

    }

    @Test
    public void setNumber() {
        Die dice = new Die(Color.BLUE);
        dice.setNumber(NumberEnum.FOUR);
        assertEquals(NumberEnum.FOUR, dice.getNumber());
    }

    @Test
    public void reRoll() {
        Die dice = new Die(Color.BLUE);
        dice.reRoll();
        for(int i = 0; i<100; i++){
            assertTrue(dice.getNumber().getInt() <= NumberEnum.SIX.getInt() && dice.getNumber().getInt() >= NumberEnum.ONE.getInt());
        }
    }

    @Test
    public void testFlip(){
        Die dice = new Die(Color.BLUE);
        int number = dice.getNumber().getInt();

        dice.flip();
        assertEquals(dice.getNumber().getInt(),7 - number);
    }

}