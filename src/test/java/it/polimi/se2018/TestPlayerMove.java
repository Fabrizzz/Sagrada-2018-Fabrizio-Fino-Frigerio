package it.polimi.se2018;

import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.messages.PlayerMove;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * PlayerMove test
 * @author Alessio
 */
public class TestPlayerMove {

    private PlayerMove playerMove;

    /**
     * Test initializing the playermove with a SKIPTURN move
     */
    @Test
    public void skipturnMartellettoTenagliaTest(){
        playerMove = new PlayerMove(Tool.SKIPTURN);
        assertEquals(Tool.SKIPTURN,playerMove.getTool());

        try{
            playerMove = new PlayerMove(Tool.ALESATOREPERLAMINADIRAME);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Test initializing the playermove with a MOSSASTANDARD move
     */
    @Test
    public void standardSugheroTest(){
        playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,2,3);
        assertEquals(Tool.MOSSASTANDARD,playerMove.getTool());
        assertEquals(1,(int) playerMove.getRow().get());
        assertEquals(2,(int) playerMove.getColumn().get());
        assertEquals(3,(int) playerMove.getDraftPosition().get());

        try{
            playerMove = new PlayerMove(Tool.ALESATOREPERLAMINADIRAME);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Test initializing the playermove with a PINZASGROSSATRICE move
     */
    @Test
    public void pinzaSgrossatriceTest(){
        playerMove = new PlayerMove(Tool.PINZASGROSSATRICE,3,true);
        assertEquals(Tool.PINZASGROSSATRICE,playerMove.getTool());
        assertEquals(3,(int) playerMove.getDraftPosition().get());
        assertEquals(true,playerMove.getAumentaValoreDado().get());

        try{
            playerMove = new PlayerMove(Tool.MOSSASTANDARD,3,true);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Test initializing the playermove with a PENNELLOPEREGLOMISE move
     */
    @Test
    public void pennelloAlesatoreLathekinTaglierinaTest(){
        playerMove = new PlayerMove(Tool.PENNELLOPEREGLOMISE,1,2,3,4);
        assertEquals(Tool.PENNELLOPEREGLOMISE,playerMove.getTool());
        assertEquals(1,(int) playerMove.getRow().get());
        assertEquals(2,(int) playerMove.getColumn().get());
        assertEquals(3,(int) playerMove.getFinalRow().get());
        assertEquals(4,(int) playerMove.getFinalColumn().get());

        try{
            playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,2,3,4);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Test initializing the playermove with a PENNELLOPEREGLOMISE move
     */
    @Test
    public void teglierinaLathekinTest(){
        PlayerMove playerMove2 = new PlayerMove(Tool.PENNELLOPEREGLOMISE,1,2,3,4);
        playerMove = new PlayerMove(Tool.TAGLIERINAMANUALE,1,2,3,4,playerMove2);
        assertEquals(Tool.TAGLIERINAMANUALE,playerMove.getTool());
        assertEquals(1,(int) playerMove.getRow().get());
        assertEquals(2,(int) playerMove.getColumn().get());
        assertEquals(3,(int) playerMove.getFinalRow().get());
        assertEquals(4,(int) playerMove.getFinalColumn().get());
        assertEquals(playerMove2,playerMove.getNextMove().get());

        try{
            playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,2,3,4,playerMove2);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Test initializing the playermove with a TAGLIERINACIRCOLARE move
     */
    @Test
    public void taglierinaCircolareTest(){
        playerMove = new PlayerMove(1,2,3,Tool.TAGLIERINACIRCOLARE);
        assertEquals(Tool.TAGLIERINACIRCOLARE,playerMove.getTool());
        assertEquals(1,(int) playerMove.getDraftPosition().get());
        assertEquals(2,(int) playerMove.getRoundTrackRound().get());
        assertEquals(3,(int) playerMove.getRoundTrackPosition().get());

        try{
            playerMove = new PlayerMove(1,2,3,Tool.MOSSASTANDARD);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Test initializing the playermove with a PENNELLOPERPASTASALDA move
     */
    @Test
    public void pennelloDiluentePastaSaldaTest(){
        playerMove = new PlayerMove(1,2,3,NumberEnum.ONE,Tool.PENNELLOPERPASTASALDA);
        assertEquals(Tool.PENNELLOPERPASTASALDA,playerMove.getTool());
        assertEquals(3,(int) playerMove.getDraftPosition().get());
        assertEquals(1,(int) playerMove.getRow().get());
        assertEquals(2,(int) playerMove.getColumn().get());
        assertEquals(NumberEnum.ONE,playerMove.getNewDiceValue().get());

        try{
            playerMove = new PlayerMove(1,2,3,NumberEnum.ONE,Tool.MOSSASTANDARD);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Test initializing the playermove with a PENNELLOPERPASTASALDA move
     */
    @Test
    public void pennelloDiluentePastaSaldaTest2(){
        playerMove = new PlayerMove(3,NumberEnum.ONE,Tool.PENNELLOPERPASTASALDA);
        assertEquals(Tool.PENNELLOPERPASTASALDA,playerMove.getTool());
        assertEquals(3,(int) playerMove.getDraftPosition().get());
        assertEquals(NumberEnum.ONE,playerMove.getNewDiceValue().get());

        try{
            playerMove = new PlayerMove(3,NumberEnum.ONE,Tool.MOSSASTANDARD);
            fail();
        }catch (IllegalArgumentException e){}
    }

    /**
     * Test initializing the playermove with a TAMPONEDIAMANTATO move
     */
    @Test
    public void tamponeDiamantatoTest(){
        playerMove = new PlayerMove(Tool.TAMPONEDIAMANTATO,1);
        assertEquals(Tool.TAMPONEDIAMANTATO,playerMove.getTool());
        assertEquals(1,(int) playerMove.getDraftPosition().get());

        try{
            playerMove = new PlayerMove(Tool.MOSSASTANDARD,1);
            fail();
        }catch (IllegalArgumentException e){}
    }


}
