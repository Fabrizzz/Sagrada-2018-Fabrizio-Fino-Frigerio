package it.polimi.se2018;

import it.polimi.se2018.model.DiceBag;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestDraftPool {

    private DiceBag dicebag;
    private DraftPool draftpool;
    private int player;

    @Before
    public void init() {
        dicebag = new DiceBag(18);
        player = 4;
        draftpool = new DraftPool(player, dicebag);
    }

    @Test
    public void rollDice() {
        assertEquals(player * 2 + 1, draftpool.size());
        while(draftpool.size() != 0){
            draftpool.removeDie(0);
        }
        draftpool.rollDice(dicebag);
        assertEquals(player*2 +1, draftpool.size());
    }

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

    @Test
    public void removeDie() throws NoDieException {   //Rimozione per indice
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

    @Test
    public void removeDie1() {   //Rimozione per dado
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
        /* try {
            die = new Die(Color.BLUE);
            draftpool.removeDie(die);
            fail();
        } catch (NoDieException e) {}*/

    }

    @Test
    public void removeAll() {
        draftpool.removeAll();
        int i = draftpool.size();
        assertEquals(i, draftpool.removeAll().size());
        assertEquals(0, draftpool.size());

    }

    @Test
    public void size() {
        int k = 0, i = draftpool.size();
        while(draftpool.size() != 0){
            draftpool.removeDie(0);
            k++;
        }
        assertEquals(i,k);
    }
}