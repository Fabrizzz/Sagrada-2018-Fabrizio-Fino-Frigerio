package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.NormalMoveHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.Tool2_3Handler;
import it.polimi.se2018.controller.chainOfResponsibilities.Tool4_12Handler;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.TestConnection;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.*;

public class Tool4HandlerTest {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private Tool4_12Handler tool4_12Handler;

    @Before
    public void initialize(){

        LOGGER.setLevel(Level.OFF);
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.FINEST);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize(Tool.LATHEKIN);
        PlayerMove playerMove2 = new PlayerMove(Tool.LATHEKIN,2,0,2,2);
        playerMove = new PlayerMove(Tool.LATHEKIN,1,0,2,1,playerMove2);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        tool4_12Handler = new Tool4_12Handler(Tool.LATHEKIN);
        NormalMoveHandler normalMoveHandler = new NormalMoveHandler();
        tool4_12Handler.setNextHandler(normalMoveHandler);
        normalMoveHandler.setNextHandler(new EndOfTheChainHandler());

        model.getDraftPool().removeAll();
        Die die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.SIX);
        model.getDraftPool().addDie(die);

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.ONE);
        model.getDraftPool().addDie(die);

        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.FIVE);
        model.getDraftPool().addDie(die);

        try{
            assertTrue(tool4_12Handler.process(new PlayerMove(Tool.MOSSASTANDARD,1,0,0),remoteView,model));
            model.setNormalMove(false);
            assertTrue(tool4_12Handler.process(new PlayerMove(Tool.MOSSASTANDARD,2,0,0),remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void wrongInitialization(){
        try{
            tool4_12Handler = new Tool4_12Handler(Tool.RIGAINSUGHERO);
            fail();
        }catch (Exception e){}
    }

    @Test
    public void notLATHEKINTest(){
        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,1,0,0);
        try{
            assertFalse(tool4_12Handler.process(playerMove,remoteView,model));
        }catch (Exception e){}
    }

    @Test
    public void singleMoveTest(){
        playerMove = new PlayerMove(Tool.LATHEKIN,1,0,2,1);
        try {
            tool4_12Handler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}
    }

    @Test
    public void doubleMoveTest(){
        try {
            assertTrue(tool4_12Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        try {
            assertFalse(tool4_12Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        assertFalse(model.getBoard(model.getPlayers().get(0)).containsDie(1,0));
        assertFalse(model.getBoard(model.getPlayers().get(0)).containsDie(2,0));

        try{
            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(2,1).getNumber(),NumberEnum.SIX);
            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(2,1).getColor(),Color.BLUE);

            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(2,2).getNumber(),NumberEnum.ONE);
            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(2,2).getColor(),Color.RED);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void doubleIllegalMoveTest1(){
        PlayerMove playerMove2 = new PlayerMove(Tool.LATHEKIN,2,0,2,1);
        playerMove = new PlayerMove(Tool.LATHEKIN,1,0,3,0,playerMove2);

        try {
            assertFalse(tool4_12Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        assertTrue(model.getBoard(model.getPlayers().get(0)).containsDie(1,0));
        assertTrue(model.getBoard(model.getPlayers().get(0)).containsDie(2,0));

        try{
            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(1,0).getNumber(),NumberEnum.SIX);
            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(1,0).getColor(),Color.BLUE);

            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(2,0).getNumber(),NumberEnum.ONE);
            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(2,0).getColor(),Color.RED);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void doubleIllegalMoveTest2(){
        PlayerMove playerMove2 = new PlayerMove(Tool.LATHEKIN,2,0,3,0);
        playerMove = new PlayerMove(Tool.LATHEKIN,1,0,2,1,playerMove2);

        try {
            assertFalse(tool4_12Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        assertTrue(model.getBoard(model.getPlayers().get(0)).containsDie(1,0));
        assertTrue(model.getBoard(model.getPlayers().get(0)).containsDie(2,0));

        try{
            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(1,0).getNumber(),NumberEnum.SIX);
            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(1,0).getColor(),Color.BLUE);

            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(2,0).getNumber(),NumberEnum.ONE);
            assertEquals(model.getBoard(model.getPlayers().get(0)).getDie(2,0).getColor(),Color.RED);
        }catch (Exception e){
            fail();
        }
    }


}
