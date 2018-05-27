package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.BoardName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPrivateObjective {

    private PlayerBoard playerBoard;
    private Die die;
    private Color color;
    private PrivateObjective card;

    @Before
    public void setUp() {

        playerBoard = new PlayerBoard(BoardName.KALEIDOSCOPICDREAM);

        //Row 0

        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.ONE);
        try {
            playerBoard.setDie(die, 0,0);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.TWO);
        try {
            playerBoard.setDie(die, 0,1);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.THREE);
        try {
            playerBoard.setDie(die, 0,2);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.PURPLE);
        die.setNumber(NumberEnum.FOUR);
        try {
            playerBoard.setDie(die, 0,3);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.ONE);
        try {
            playerBoard.setDie(die, 0,4);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        //Row 1

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.SIX);
        try {
            playerBoard.setDie(die, 1,0);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.PURPLE);
        die.setNumber(NumberEnum.ONE);
        try {
            playerBoard.setDie(die, 1,1);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.FIVE);
        try {
            playerBoard.setDie(die, 1,2);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.TWO);
        try {
            playerBoard.setDie(die, 1,3);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.FOUR);
        try {
            playerBoard.setDie(die, 1,4);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        //Row 2

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.THREE);
        try {
            playerBoard.setDie(die, 2,0);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.TWO);
        try {
            playerBoard.setDie(die, 2,1);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.THREE);
        try {
            playerBoard.setDie(die, 2,2);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.PURPLE);
        die.setNumber(NumberEnum.FOUR);
        try {
            playerBoard.setDie(die, 2,3);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.THREE);
        try {
            playerBoard.setDie(die, 2,4);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }


        //Row 3

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.TWO);
        try {
            playerBoard.setDie(die, 3,0);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.PURPLE);
        die.setNumber(NumberEnum.SIX);
        try {
            playerBoard.setDie(die, 3,1);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.ONE);
        try {
            playerBoard.setDie(die, 3,2);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }

        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.SIX);
        try {
            playerBoard.setDie(die, 3,4);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }
    }

    @Test
    public void getPoints() {

        int point;

        //Yellow
        card = new PrivateObjective(Color.YELLOW);
        point = card.getPoints(playerBoard);
        assertEquals(11, point);

        //Blue
        card = new PrivateObjective(Color.BLUE);
        point = card.getPoints(playerBoard);
        assertEquals(9, point);

        //Red
        card = new PrivateObjective(Color.RED);
        point = card.getPoints(playerBoard);
        assertEquals(11, point);

        //Purple
        card = new PrivateObjective(Color.PURPLE);
        point = card.getPoints(playerBoard);
        assertEquals(15, point);

        //Green
        card = new PrivateObjective(Color.GREEN);
        point = card.getPoints(playerBoard);
        assertEquals(13, point);

        //Full board
        die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.FIVE);
        try {
            playerBoard.setDie(die, 3,3);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }
        card = new PrivateObjective(Color.BLUE);
        point = card.getPoints(playerBoard);
        assertEquals(14, point);

        //Empty board
        playerBoard = new PlayerBoard(BoardName.KALEIDOSCOPICDREAM);
        card = new PrivateObjective(Color.BLUE);
        point = card.getPoints(playerBoard);
        assertEquals(0, point);

        //One element
        die = new Die(Color.RED);
        die.setNumber(NumberEnum.THREE);
        try {
            playerBoard.setDie(die, 2,2);
        } catch (AlredySetDie alredySetDie) {
            fail();
        }
        card = new PrivateObjective(Color.RED);
        point = card.getPoints(playerBoard);
        assertEquals(3, point);
    }
}