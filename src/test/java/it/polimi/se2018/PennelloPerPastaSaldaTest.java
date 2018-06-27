package it.polimi.se2018;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.PennelloPerPastaSaldaHandler;
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

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PennelloPerPastaSaldaTest {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private PennelloPerPastaSaldaHandler pennelloPerPastaSaldaHandler;

    @Before
    public void initialize(){

        LOGGER.setLevel(Level.FINEST);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize(Tool.PENNELLOPERPASTASALDA);

        playerMove = new PlayerMove(1,0,0,NumberEnum.ONE,Tool.PENNELLOPERPASTASALDA);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        pennelloPerPastaSaldaHandler = new PennelloPerPastaSaldaHandler();
        pennelloPerPastaSaldaHandler.setNextHandler(new EndOfTheChainHandler());
    }

    @Test
    public void notPennelloPerPastaSaldaTest(){
        playerMove = new PlayerMove(Tool.MARTELLETTO);
        try{
            assertFalse(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void initialPositionTest(){
        model =  ModelControllerInitializerTest.initialize();
        try{
            pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}


        model = ModelControllerInitializerTest.initialize(Tool.PENNELLOPERPASTASALDA);
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        model.setUsedTool(false);
        playerMove = new PlayerMove(1,2,0,NumberEnum.ONE,Tool.PENNELLOPERPASTASALDA);
        try{
            assertFalse(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        playerMove = new PlayerMove(1,0,0,NumberEnum.ONE,Tool.PENNELLOPERPASTASALDA);
        try{
            assertTrue(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }


    @Test
    public void doubleInsertTest(){
        try{
            assertTrue(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        try{
            assertFalse(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        try{
            assertFalse(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void notNearTest(){
        try{
            assertTrue(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        playerMove = new PlayerMove(3,4,0,NumberEnum.SIX,Tool.PENNELLOPERPASTASALDA);
        try{
            assertFalse(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void noInsertTest1(){
        playerMove = new PlayerMove(0,NumberEnum.SIX,Tool.PENNELLOPERPASTASALDA);
        try{
            assertFalse(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        playerMove = new PlayerMove(1,0,0,NumberEnum.ONE,Tool.PENNELLOPERPASTASALDA);
        try{
            assertTrue(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        playerMove = new PlayerMove(0,NumberEnum.SIX,Tool.PENNELLOPERPASTASALDA);
        try{
            assertFalse(pennelloPerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }
}
