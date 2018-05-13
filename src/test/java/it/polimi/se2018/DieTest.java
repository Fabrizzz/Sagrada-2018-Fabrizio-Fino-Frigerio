package it.polimi.se2018;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.NumberEnum;
import org.junit.Test;

import static org.junit.Assert.*;

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
            result = dice.getNumber().getNumber() <= NumberEnum.SIX.getNumber() && dice.getNumber().getNumber() >= NumberEnum.ONE.getNumber();
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
        boolean result;
        Die dice = new Die(Color.BLUE);
        dice.reRoll();
        for(int i = 0; i<100; i++){
            result = dice.getNumber().getNumber() <= NumberEnum.SIX.getNumber() && dice.getNumber().getNumber() >= NumberEnum.ONE.getNumber();
            assertTrue(result);
        }
    }

}