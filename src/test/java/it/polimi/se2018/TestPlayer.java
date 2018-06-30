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

    @Before
    public void initialize(){
        player = new Player("Alessio",(long) 1234);
    }

    @Test
    public void isSkipSecondTurnTest(){
        player.setSkipSecondTurn(true);
        assertTrue(player.isSkipSecondTurn());
    }

    @Test
    public void getFlavorTokens(){
        player.setFavorTokens(3);
        assertEquals(3,player.getFavorTokens());
    }

    @Test
    public void isCanDoTwoTurns(){
        player.setCanDoTwoTurn(true);
        assertTrue(player.isCanDoTwoTurn());
    }

    @Test
    public void getNickTest(){
        assertEquals("Alessio",player.getNick());
    }

    @Test
    public void getIdTest(){
        assertEquals(1234,(long) player.getId());
    }

    @Test
    public void isYourTurnTest(){
        assertFalse(player.isYourTurn());
        player.setYourTurn(true);
        assertTrue(player.isYourTurn());
    }


}
