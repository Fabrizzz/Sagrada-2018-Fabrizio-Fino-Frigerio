package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.RigaInSugheroHandler;
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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class RigaInSugheroHandlerTest {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private RigaInSugheroHandler rigaInSugheroHandler;
    @Before
    public void initialize(){

        LOGGER.setLevel(Level.OFF);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize(Tool.RIGAINSUGHERO);

        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,1,0,0);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        rigaInSugheroHandler = new RigaInSugheroHandler();
        rigaInSugheroHandler.setNextHandler(new EndOfTheChainHandler());

        model.getDraftPool().removeAll();
        Die die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.SIX);
        model.getDraftPool().addDie(die);

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.ONE);
        model.getDraftPool().addDie(die);
    }

    @Test
    public void notRigaInSugheroTest(){
        playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,0,0);
        try{
            assertFalse(rigaInSugheroHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void negativeParameterTest(){
        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,-1,0,0);
        try{
            rigaInSugheroHandler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,1,-1,0);
        try{
            rigaInSugheroHandler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,0,0,-1);
        try{
            rigaInSugheroHandler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}


        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,4,0,0);
        try{
            rigaInSugheroHandler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,1,5,0);
        try{
            rigaInSugheroHandler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,0,0,model.getDraftPool().size());
        try{
            rigaInSugheroHandler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}
    }

    @Test
    public void initialInsertTest(){
        try{
            assertTrue(rigaInSugheroHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void usedCellTest(){
        try{
            assertTrue(rigaInSugheroHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        try{
            assertFalse(rigaInSugheroHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void secondNearInsertTest(){
        try{
            assertTrue(rigaInSugheroHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,2,0,0);
        try{
            assertFalse(rigaInSugheroHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void secondFarInsertTest(){
        try{
            assertTrue(rigaInSugheroHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,0,4,0);
        try{
            assertTrue(rigaInSugheroHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }
}
