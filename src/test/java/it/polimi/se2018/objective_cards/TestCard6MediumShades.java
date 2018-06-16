package it.polimi.se2018.objective_cards;

import it.polimi.se2018.model.BoardList;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestCard6MediumShades {

    private PlayerBoard playerBoard;
    private Die die;
    private PublicObjective card;
    private BoardList BoardList = new BoardList();

    @Before
    public void setUp() {
        card = card = PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.SFUMATUREMEDIE);
        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));

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
        assertEquals(6, point);

        try {
            playerBoard.removeDie(0,2);
        } catch (NoDieException e) {
            fail();
        }
        try {
            playerBoard.removeDie(2,2);
        } catch (NoDieException e) {
            fail();
        }
        point = card.getPoints(playerBoard);
        assertEquals(4, point);

        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));
        point = card.getPoints(playerBoard);
        assertEquals(0, point);
    }
}