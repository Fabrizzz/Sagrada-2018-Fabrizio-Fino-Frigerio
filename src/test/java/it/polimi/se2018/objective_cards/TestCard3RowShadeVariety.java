package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.BoardName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class TestCard3RowShadeVariety {

    private PlayerBoard playerBoard;
    private Die die;
    private ArrayList<Integer> rowNum;
    private Card3RowShadeVariety card;

    @Before
    public void setUp() {

        rowNum = new ArrayList<Integer>();
        card = new Card3RowShadeVariety(PublicObjectiveName.SFUMATUREDIVERSERIGA);
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

        point = card.getPoints(playerBoard);
        assertEquals(5, point);

        playerBoard = new PlayerBoard(BoardName.KALEIDOSCOPICDREAM);
        point = card.getPoints(playerBoard);
        assertEquals(0, point);

    }

    @Test
    public void controlNumRow() {

        boolean bool;

        bool = card.controlNumRow(rowNum);
        assertFalse(bool);

        rowNum.add(1);
        rowNum.add(2);
        rowNum.add(3);
        rowNum.add(5);
        rowNum.add(6);

        bool = card.controlNumRow(rowNum);
        assertTrue(bool);

        rowNum.add(1);
        bool = card.controlNumRow(rowNum);
        assertFalse(bool);
    }

    @Test
    public void removeAll() {

        int size;

        size = 0;
        card.removeAll(rowNum);
        assertEquals(size, rowNum.size());

        rowNum.add(1);
        rowNum.add(2);
        rowNum.add(3);
        rowNum.add(5);
        rowNum.add(6);

        size = 5;
        assertEquals(size, rowNum.size());

        size = 0;
        card.removeAll(rowNum);
        assertEquals(size, rowNum.size());
    }
}