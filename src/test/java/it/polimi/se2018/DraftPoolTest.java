package it.polimi.se2018;

import it.polimi.se2018.model.DiceBag;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DraftPoolTest {

    @Test
    public void rollDice() {
        int player = 4;
        int j = 18;
        DiceBag dicebag = new DiceBag(j);
        DraftPool draftpool = new DraftPool(player, dicebag);
        while(draftpool.size() != 0){
            draftpool.removeDie(0);
        }
        draftpool.rollDice(dicebag);
        assertEquals(player*2 +1, draftpool.size());
    }

    @Test
    public void reRollDice() {
        int player = 4;
        int j = 18;
        int i,k,n;
        DiceBag dicebag = new DiceBag(j);
        DraftPool draftpool = new DraftPool(player, dicebag);
        List<Die> drafpool_temp = new ArrayList<>();
        for(n = 0; n < draftpool.size(); n ++){
            try {
                drafpool_temp.add(draftpool.getDie(n));
            } catch (NoDieException e) {
                fail();
            }
        }
        i = draftpool.size();
        draftpool.reRollDice();
        k = draftpool.size();
        assertEquals(i,k);
        for(n = 0; n < draftpool.size(); n ++){
            try {
                assertEquals(drafpool_temp.get(n), draftpool.getDie(n));
            } catch (NoDieException e) {
                fail();
            }
        }

    }

    @Test
    public void getDie() {
        int player = 4;
        int j = 18;
        DiceBag dicebag = new DiceBag(j);
        DraftPool draftpool = new DraftPool(player, dicebag);
        Die dice1 = new Die(Color.BLUE);
        Die dice2;
        draftpool.addDie(dice1);
        try {
            dice2 = draftpool.getDie(draftpool.size() - 1);
            assertEquals(dice1, dice2);
        } catch (NoDieException e) {
            fail();
        }
    }

    @Test
    public void addDie() {
        int player = 4;
        int j = 18;
        int i;
        DiceBag dicebag = new DiceBag(j);
        DraftPool draftpool = new DraftPool(player, dicebag);
        Die dice1 = new Die(Color.BLUE);
        Die dice2;
        i = draftpool.size();
        draftpool.addDie(dice1);
        try {
            dice2 = draftpool.getDie(draftpool.size() - 1);
            assertEquals(i + 1,draftpool.size());
            assertEquals(dice1, dice2);
        } catch (NoDieException e) {
            fail();
        }
    }

    @Test
    public void removeDie() throws NoDieException {   //Rimozione per indice
        int player = 4;
        int j = 18;
        int i;
        DiceBag dicebag = new DiceBag(j);
        DraftPool draftpool = new DraftPool(player, dicebag);
        i = draftpool.size();
        Die dice;
        try {
            dice = draftpool.getDie(0);
            draftpool.removeDie(0);
            assertEquals(i-1, draftpool.size());
            assertNotEquals(dice, draftpool.getDie(0));
        } catch (NoDieException e) {
            fail();
        }
    }

    @Test
    public void removeDie1() {   //Rimozione per dado
        int player = 4;
        int j = 18;
        int i;
        DiceBag dicebag = new DiceBag(j);
        DraftPool draftpool = new DraftPool(player, dicebag);
        i = draftpool.size();
        Die dice;
        try {
            dice = draftpool.getDie(0);
            draftpool.removeDie(dice);
            assertEquals(i-1, draftpool.size());
            assertNotEquals(dice, draftpool.getDie(0));
        } catch (NoDieException e) {
            fail();
        }
    }

    @Test
    public void removeAll() {
        int player = 4;
        int j = 18;
        DiceBag dicebag = new DiceBag(j);
        DraftPool draftpool = new DraftPool(player, dicebag);
        draftpool.removeAll();
        assertEquals(0, draftpool.size());

    }

    @Test
    public void size() {
        int player = 4;
        int j = 18;
        int i,k = 0;
        DiceBag dicebag = new DiceBag(j);
        DraftPool draftpool = new DraftPool(player, dicebag);
        i = draftpool.size();
        while(draftpool.size() != 0){
            draftpool.removeDie(0);
            k++;
        }
        assertEquals(i,k);
    }
}