package it.polimi.se2018;

import it.polimi.se2018.model.DiceBag;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.EmptyBagException;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Matteo
 */
public class TestDiceBag {

    private DiceBag dicebag;

    /**
     * Initialize the dicebag
     */
    @Before
    public void initDiceBag() {
        dicebag = new DiceBag(18);
    }

    /**
     * Test taking a die from the dicebag
     */
    @Test
    public void testTakeDie() {
        int i = 0;
        Color rand = Color.values()[new Random().nextInt(Color.values().length)];
        while(dicebag.size() != 0){
            try {
                if (dicebag.takeDie().getColor() == rand) {
                    i++;
                }
            } catch (EmptyBagException e) {
                fail();
            }
        }
        assertEquals(18,i);
    }

    /**
     * test adding a die to the dicebag
     */
    @Test
    public void testAddDie() {
        int i = 0;
        int k;
        Die die = new Die(Color.BLUE);
        k = dicebag.size();
        dicebag.addDie(die);
        assertEquals(k + 1, dicebag.size());
        while(dicebag.size() != 0){
            try {
                if(dicebag.takeDie().getColor() == Color.BLUE){
                    i++;
                }
            } catch (EmptyBagException e) {
                fail();
            }
        }
        assertEquals(19,i);

    }

    /**
     * test getting the size of the dicebag
     */
    @Test
    public void testSize() {
        assertEquals(Color.values().length * 18, dicebag.size());
        int i = 0;
        int k;
        k = dicebag.size();
        while(dicebag.size() != 0){
            try {
                dicebag.takeDie();
            } catch (EmptyBagException e) {
                fail();
            }
            i++;
            }
        assertEquals(i, k);
    }
}