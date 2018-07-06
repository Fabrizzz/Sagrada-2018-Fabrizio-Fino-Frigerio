package it.polimi.se2018;

import it.polimi.se2018.model.Player;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Alessio
 */
public class TestPlayer {
    private Player player;

    /**
     * initialize the player for the test
     */
    @Before
    public void initialize(){
        player = new Player("Alessio",(long) 1234);
    }

    /**
     * test if the player must skip the second turn
     */
    @Test
    public void isSkipSecondTurnTest(){
        player.setSkipSecondTurn(true);
        assertTrue(player.isSkipSecondTurn());
    }

    /**
     * test the favortokens
     */
    @Test
    public void getFlavorTokens(){
        player.setFavorTokens(3);
        assertEquals(3,player.getFavorTokens());
    }

    /**
     * test if the player can do two turns
     */
    @Test
    public void isCanDoTwoTurns(){
        player.setCanDoTwoTurn(true);
        assertTrue(player.isCanDoTwoTurn());
    }

    /**
     * test getting the nickname
     */
    @Test
    public void getNickTest(){
        assertEquals("Alessio",player.getNick());
    }

    /**
     * test getting the id
     */
    @Test
    public void getIdTest(){
        assertEquals(1234,(long) player.getId());
    }

    /**
     * test checking if is the player turn
     */
    @Test
    public void isYourTurnTest(){
        assertFalse(player.isYourTurn());
        player.setYourTurn(true);
        assertTrue(player.isYourTurn());
    }


}
