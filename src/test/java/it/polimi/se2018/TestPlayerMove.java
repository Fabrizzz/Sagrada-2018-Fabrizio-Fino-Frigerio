package it.polimi.se2018;

import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.enums.Tool;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class TestPlayerMove {

    private PlayerMove playerMove;

    @Test
    public void skipturnMartellettoTenagliaTest(){
        playerMove = new PlayerMove(Tool.SKIPTURN);
        assertEquals(Tool.SKIPTURN,playerMove.getTool());

        try{
            playerMove = new PlayerMove(Tool.ALESATOREPERLAMINADIRAME);
            fail();
        }catch (IllegalArgumentException e){}
    }

   /* @Test
    public void standardRigaTest(){
        playerMove = new PlayerMove(Tool.MOSSASTANDARD,0,1,2);
        assertEquals(0,playerMove.getRow());
        assertEquals(1,playerMove.getColumn());
        assertEquals(2,playerMove.getDraftPosition());
    }*/
}
