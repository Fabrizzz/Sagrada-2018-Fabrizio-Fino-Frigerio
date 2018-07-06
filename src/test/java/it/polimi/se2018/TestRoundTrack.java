package it.polimi.se2018;

import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Giampietro,Alessio,Matteo
 */
public class TestRoundTrack {

    private RoundTrack roundtrack;
    private int round;

    /**
     * initialize the roundtrack
     */
    @Before
    public void init() {
        round = 5;
        roundtrack = new RoundTrack();

    }

    /**
     * test getting a die from the roundtrack
     */
    @Test
    public void getDie() {
        int i;
        Die die = new Die(Color.BLUE);
        i = roundtrack.numberOfDice(round);
        roundtrack.addDie(round, die);
        try{
            assertEquals(die, roundtrack.getDie(round, i));
        }catch (NoDieException e){
            fail();
        }
    }

    /**
     * test adding a die from the roundtrack
     */
    @Test
    public void addDie() {
        int i;
        Die die = new Die(Color.BLUE);
        i = roundtrack.numberOfDice(round);
        roundtrack.addDie(round, die);
        try{
            assertEquals(die, roundtrack.getDie(round, i));
        }catch (NoDieException e){
            fail();
        }
        assertEquals(i+1, roundtrack.numberOfDice(round));
    }

    /**
     * testing the number of dice in the roundtrack
     */
    @Test
    public void numberOfDice() {
        RoundTrack roundtrack = new RoundTrack();
        roundtrack.addDie(round, new Die(Color.BLUE));
        roundtrack.addDie(round, new Die(Color.BLUE));
        roundtrack.addDie(round, new Die(Color.BLUE));
        assertEquals(3, roundtrack.numberOfDice(round));
    }

    /**
     * testing if the roundtrack contains a color
     */
    @Test
    public void hasColor() {
        RoundTrack roundtrack = new RoundTrack();
        Die dice1 = new Die(Color.BLUE);
        Die dice2 = new Die(Color.RED);
        Die dice3 = new Die(Color.PURPLE);
        roundtrack.addDie(round,dice1);
        roundtrack.addDie(round,dice2);
        roundtrack.addDie(round,dice3);
        assertTrue(roundtrack.hasColor(Color.BLUE));

        assertFalse(roundtrack.hasColor(Color.GREEN));

    }

    /**
     * test removing a die from the roundtrack
     */
    @Test
    public void testRemoveDie(){
        RoundTrack roundtrack = new RoundTrack();
        Die dice = new Die(Color.BLUE);

        try{
            roundtrack.removeDie(round, 0);
            fail();
        }catch(NoDieException e){}

        roundtrack.addDie(round, dice);
        try{
            roundtrack.removeDie(round, 0);
        }catch(NoDieException e){
            fail();
        }

        try{
            roundtrack.removeDie(round, 0);
            fail();
        }catch(NoDieException e){}

    }
}