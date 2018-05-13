package it.polimi.se2018;

import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Test;

import java.util.prefs.NodeChangeEvent;

import static org.junit.Assert.*;

public class RoundTrackTest {

    @Test
    public void getDie() {
        int i;
        int round = 5;
        RoundTrack roundtrack = new RoundTrack();
        Die dice = new Die(Color.BLUE);
        Die dice_temp;
        i = roundtrack.numberOfDice(round);
        roundtrack.addDie(round,dice);
        dice_temp = roundtrack.getDie(round, i);
        assertEquals(dice,dice_temp);
    }

    @Test
    public void addDie() {
        int i;
        int round = 5;
        RoundTrack roundtrack = new RoundTrack();
        Die dice = new Die(Color.BLUE);
        Die dice_temp;
        i = roundtrack.numberOfDice(round);
        roundtrack.addDie(round,dice);
        dice_temp = roundtrack.getDie(round, i);
        assertEquals(dice,dice_temp);
        assertEquals(i+1, roundtrack.numberOfDice(round));
    }

    @Test
    public void numberOfDice() {
        int round = 5;
        RoundTrack roundtrack = new RoundTrack();
        Die dice1 = new Die(Color.BLUE);
        Die dice2 = new Die(Color.BLUE);
        Die dice3 = new Die(Color.BLUE);
        roundtrack.addDie(round,dice1);
        roundtrack.addDie(round,dice2);
        roundtrack.addDie(round,dice3);
        assertEquals(3, roundtrack.numberOfDice(round));
    }

    @Test
    public void hasColor() {
        int round = 5;
        boolean colore;
        RoundTrack roundtrack = new RoundTrack();
        Die dice1 = new Die(Color.BLUE);
        Die dice2 = new Die(Color.RED);
        Die dice3 = new Die(Color.PURPLE);
        roundtrack.addDie(round,dice1);
        roundtrack.addDie(round,dice2);
        roundtrack.addDie(round,dice3);
        colore = roundtrack.hasColor(Color.BLUE);
        assertTrue(colore);
        colore = roundtrack.hasColor(Color.GREEN);
        assertFalse(colore);
    }

    @Test
    public void testRemoveDie(){
        RoundTrack roundtrack = new RoundTrack();
        Die dice = new Die(Color.BLUE);

        try{
            roundtrack.removeDie(0,0);
            fail();
        }catch(NoDieException e){}

        roundtrack.addDie(0,dice);
        try{
            roundtrack.removeDie(0,0);
        }catch(NoDieException e){
            fail();
        }

        try{
            roundtrack.removeDie(0,roundtrack.numberOfDice(0));
            fail();
        }catch(NoDieException e){}

    }
}