package it.polimi.se2018;

import it.polimi.se2018.model.DiceBag;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Giampietro
 */
public class TestDraftPool {

    private DiceBag dicebag;
    private DraftPool draftpool;
    private int player;

    /**
     * initialize the draftpool
     */
    @Before
    public void init() {
        dicebag = new DiceBag(18);
        player = 4;
        draftpool = new DraftPool(player, dicebag);
    }

    /**
     * test rolling the dice
     */
    @Test
    public void rollDice() {
        assertEquals(player * 2 + 1, draftpool.size());
        while(draftpool.size() != 0){
            try {
                draftpool.removeDie(0);
            } catch (NoDieException e) {
                fail();
            }
        }
        draftpool.rollDice(dicebag);
        assertEquals(player*2 +1, draftpool.size());
    }

    /**
     * test rerolling the dice
     */
    @Test
    public void reRollDice() {
        int old_size;
        List<Die> drafpool_temp = new ArrayList<>();
        for (int n = 0; n < draftpool.size(); n++) {
            try {
                drafpool_temp.add(draftpool.getDie(n));
            } catch (NoDieException e) {
                fail();
            }
        }
        old_size = draftpool.size();
        draftpool.reRollDice();
        assertEquals(old_size, draftpool.size());

        for (int n = 0; n < draftpool.size(); n++) {
            try {
                assertEquals(drafpool_temp.get(n), draftpool.getDie(n));
            } catch (NoDieException e) {
                fail();
            }
        }

    }

    /**
     * test getting the dice
     */
    @Test
    public void getDie() {
        Die die = new Die(Color.BLUE);
        draftpool.addDie(die);

        try {
            assertEquals(die, draftpool.getDie(draftpool.size() - 1));
        } catch (NoDieException e) {
            fail();
        }

        try{
            draftpool.getDie(draftpool.size());
            fail();
        } catch (NoDieException e) {}
    }

    /**
     * test adding a die
     */
    @Test
    public void addDie() {
        Die die = new Die(Color.BLUE);
        int i = draftpool.size();
        draftpool.addDie(die);
        try {
            assertEquals(i + 1,draftpool.size());
            assertEquals(die, draftpool.getDie(draftpool.size() - 1));
        } catch (NoDieException e) {
            fail();
        }
    }

    /**
     * test removing a die
     * @throws NoDieException if there is no die
     */
    @Test
    public void removeDie() throws NoDieException {
        int i = draftpool.size();
        try {
            Die die = draftpool.getDie(0);
            draftpool.removeDie(0);
            assertEquals(i-1, draftpool.size());
            assertNotEquals(die, draftpool.getDie(0));
        } catch (NoDieException e) {
            fail();
        }
    }

    /**
     * test removing a die
     */
    @Test
    public void removeDie1() {
        int i = draftpool.size();
        Die die;
        try {
            die = draftpool.getDie(0);
            draftpool.removeDie(die);
            assertEquals(i-1, draftpool.size());
            assertNotEquals(die, draftpool.getDie(0));
        } catch (NoDieException e) {
            fail();
        }
    }

    /**
     * test renivubg all the dice
     */
    @Test
    public void removeAll() {
        draftpool.removeAll();
        int i = draftpool.size();
        assertEquals(i, draftpool.removeAll().size());
        assertEquals(0, draftpool.size());

    }

    /**
     * test getting the size
     */
    @Test
    public void size() {
        int k = 0, i = draftpool.size();
        while(draftpool.size() != 0){
            try {
                draftpool.removeDie(0);
            } catch (NoDieException e) {
                fail();
            }
            k++;
        }
        assertEquals(i,k);
    }
}