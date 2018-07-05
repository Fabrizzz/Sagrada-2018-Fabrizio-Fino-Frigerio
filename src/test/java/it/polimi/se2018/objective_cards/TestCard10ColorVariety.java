package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.BoardList;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.AlreadySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestCard10ColorVariety {

    private PlayerBoard playerBoard;
    private Die die;
    private PublicObjective card;
    private int[] color;
    private BoardList BoardList = new BoardList();

    @Before
    public void setUp() {
        card = PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.VARIETADICOLORE);
        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));
        color = new int[5];

        for (int i = 0; i < 5; i++) {
            color[i] = 0;
        }

        //Row 0

        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.ONE);
        try {
            playerBoard.setDie(die, 0,0);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.TWO);
        try {
            playerBoard.setDie(die, 0,1);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.THREE);
        try {
            playerBoard.setDie(die, 0,2);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.PURPLE);
        die.setNumber(NumberEnum.FOUR);
        try {
            playerBoard.setDie(die, 0,3);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.ONE);
        try {
            playerBoard.setDie(die, 0,4);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        //Row 1

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.SIX);
        try {
            playerBoard.setDie(die, 1,0);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.PURPLE);
        die.setNumber(NumberEnum.ONE);
        try {
            playerBoard.setDie(die, 1,1);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.FIVE);
        try {
            playerBoard.setDie(die, 1,2);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.TWO);
        try {
            playerBoard.setDie(die, 1,3);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.FOUR);
        try {
            playerBoard.setDie(die, 1,4);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        //Row 2

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.THREE);
        try {
            playerBoard.setDie(die, 2,0);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.TWO);
        try {
            playerBoard.setDie(die, 2,1);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.THREE);
        try {
            playerBoard.setDie(die, 2,2);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.PURPLE);
        die.setNumber(NumberEnum.FOUR);
        try {
            playerBoard.setDie(die, 2,3);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.THREE);
        try {
            playerBoard.setDie(die, 2,4);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        //Row 3

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.TWO);
        try {
            playerBoard.setDie(die, 3,0);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.PURPLE);
        die.setNumber(NumberEnum.SIX);
        try {
            playerBoard.setDie(die, 3,1);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.GREEN);
        die.setNumber(NumberEnum.ONE);
        try {
            playerBoard.setDie(die, 3,2);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.SIX);
        try {
            playerBoard.setDie(die, 3,4);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }
    }

    @Test
    public void getPoints() {

        int point;

        point = card.getPoints(playerBoard);
        assertEquals(12, point);

        try {
            playerBoard.removeDie(3,0);
        } catch (NoDieException e) {
            fail();
        }
        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.TWO);
        try {
            playerBoard.setDie(die, 3,0);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }
        die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.FIVE);
        try {
            playerBoard.setDie(die, 3,3);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }
        point = card.getPoints(playerBoard);
        assertEquals(16, point);

        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));
        point = card.getPoints(playerBoard);
        assertEquals(0, point);
    }
}