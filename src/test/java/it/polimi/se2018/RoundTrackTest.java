package it.polimi.se2018;

import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.Color;
import org.junit.Test;

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
}