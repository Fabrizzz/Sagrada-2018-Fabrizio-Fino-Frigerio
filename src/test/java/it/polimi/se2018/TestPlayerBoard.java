package it.polimi.se2018;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.BoardList;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.exceptions.AlreadySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Giampietro
 */
public class TestPlayerBoard {
    private PlayerBoard playerBoard;
    private BoardList BoardList = new BoardList();

    /**
     * test getting a die from the playerboard
     */
    @Test
    public void testGetDie(){
        Die die = new Die(Color.BLUE);

        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {


                try {
                    playerBoard.getDie(i, j);
                    fail();
                } catch (NoDieException e) {
                }


                try {
                    playerBoard.setDie(die, i, j);
                } catch (AlreadySetDie e) {
                    fail();
                }

                try {
                    assertEquals(playerBoard.getDie(i, j), die);
                } catch (NoDieException e) {
                    fail();
                }
            }
        }
        }


    /**
     * test id the playerboard contains a die in a cell
     */
    @Test
    public void testContainsDie() {

        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {

                assertFalse(playerBoard.containsDie(i, j));


                Die dice = new Die(Color.BLUE);
                try {
                    playerBoard.setDie(dice, i, j);
                } catch (AlreadySetDie e) {
                    fail();
                }

                assertTrue(playerBoard.containsDie(i, j));
            }
        }
    }


    /**
     * test removing a die from the board
     */
    @Test
    public void testRemoveDice(){
        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                try {
                    playerBoard.removeDie(0, 0);
                    fail();
                } catch (NoDieException e) {
                }

                Die dice = new Die(Color.BLUE);
                try {
                    playerBoard.setDie(dice, 0, 0);
                } catch (AlreadySetDie e) {
                    fail();
                }

                try {
                    playerBoard.removeDie(0, 0);
                } catch (NoDieException e) {
                    fail();
                }

                try {
                    playerBoard.getDie(0, 0);
                    fail();
                } catch (NoDieException e) {
                }
            }
        }

    }

    /**
     * test verifying the color restrictions of a cell
     */
    @Test
    public void testVerifyColorRestriction(){

        for (Board boardName : BoardList.values()) {

            playerBoard = new PlayerBoard(boardName);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    if (playerBoard.getRestriction(i, j).isColorRestriction()) {
                        assertTrue(playerBoard.verifyColorRestriction(new Die(Color.valueOf(boardName.getRestriction(i, j))), i, j));
                        for (Color color : Color.values()) {
                            if (color != Color.valueOf(boardName.getRestriction(i, j))) {
                                assertFalse(playerBoard.verifyColorRestriction(new Die(color), i, j));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * test verifying the number restriction of a cell
     */
    @Test
    public void testVerifyNumberRestriction(){
        Die die = new Die(Color.BLUE);
        for (Board boardName : BoardList.values()) {

            playerBoard = new PlayerBoard(boardName);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    if (playerBoard.getRestriction(i, j).isNumberRestriction()) {
                        if (die.getNumber() == NumberEnum.valueOf(boardName.getRestriction(i, j)))
                            assertTrue(playerBoard.verifyNumberRestriction(die, i, j));
                        else
                            assertFalse(playerBoard.verifyNumberRestriction(die, i, j));

                    }
                }
            }
        }
    }

    /**
     * test verifying the position restriction of a cell
     */
    @Test
    public void testVerifyPositionRestriction(){
        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                assertFalse(playerBoard.verifyPositionRestriction(i, j));
            }
        }
        try {
            playerBoard.setDie(new Die(Color.BLUE), 2, 2);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                if (i != 0 || j != 0)
                    assertTrue(playerBoard.verifyPositionRestriction(2 + i, 2 + j));
    }

    /**
     * test verifying the proximity restriction of a cell
     */
    @Test
    public void testVerifyNearCellsRestriction(){
        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));
        Die die = new Die(Color.BLUE);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                assertTrue(playerBoard.verifyNearCellsRestriction(die, i, j));
            }
        }

        try {
            playerBoard.setDie(die, 2, 2);
        } catch (AlreadySetDie alreadySetDie) {
            fail();
        }

        for (int i = -1; i <= 1; i++) {
            if (i != 0) {
                assertFalse(playerBoard.verifyNearCellsRestriction(die, 2 + i, 2));
                assertFalse(playerBoard.verifyNearCellsRestriction(die, 2, 2 + 1));
            }
        }

        for (int i = -1; i <= 1; i++) {
            if (i != 0) {
                assertTrue(playerBoard.verifyNearCellsRestriction(die, 2 + i, 2 + i));
                assertTrue(playerBoard.verifyNearCellsRestriction(die, 2 + i, 2 - 1));
            }
        }
    }

    /**
     * test verifying the first die insert
     */
    @Test
    public void testInitialPositionRestriction(){
        playerBoard = new PlayerBoard(BoardList.getBoard("Kaleidoscopic Dream"));
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++) {
                if (i == 0 || i == 3 || j == 0 || j == 4)
                    assertTrue(playerBoard.verifyInitialPositionRestriction(i, j));
                else
                    assertFalse(playerBoard.verifyInitialPositionRestriction(i, j));
            }
    }

}
