package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.NormalMoveHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.Tool2_3Handler;
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

import static org.junit.Assert.*;

/**
 * @author Alessio
 */
public class TestTool2Handler {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private Tool2_3Handler tool2_3Handler;

    /**
     * Initialize the model, the handler and the draftpool for the tests
     */
    @Before
    public void initialize(){

        LOGGER.setLevel(Level.OFF);
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.FINEST);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize(Tool.PENNELLOPEREGLOMISE);

        playerMove = new PlayerMove(Tool.PENNELLOPEREGLOMISE,1,0,2,0);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        tool2_3Handler = new Tool2_3Handler(Tool.PENNELLOPEREGLOMISE);
        NormalMoveHandler normalMoveHandler = new NormalMoveHandler();
        tool2_3Handler.setNextHandler(normalMoveHandler);
        normalMoveHandler.setNextHandler(new EndOfTheChainHandler());

        model.getDraftPool().removeAll();
        Die die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.SIX);
        model.getDraftPool().addDie(die);


        die = new Die(Color.RED);
        die.setNumber(NumberEnum.ONE);
        model.getDraftPool().addDie(die);
    }

    /**
     * Try to initialize the handler with the wrong tool type
     */
    @Test
    public void wrongInitialization(){
        try{
            tool2_3Handler = new Tool2_3Handler(Tool.RIGAINSUGHERO);
            fail();
        }catch (Exception e){}
    }

    /**
     * Try to use an tool not present in the chain of responsibilities
     */
    @Test
    public void notTool2_3Test(){
        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,1,0,0);
        try{
            assertFalse(tool2_3Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Try to use the tool with no die present in the board
     */
    @Test
    public void noDieMoveTest(){
        try{
            assertFalse(tool2_3Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Try to use the tool on a single die
     */
    @Test
    public void oneDieMove(){
        try{
            assertTrue(tool2_3Handler.process(new PlayerMove(Tool.MOSSASTANDARD,1,0,0),remoteView,model));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        try{
            assertTrue(tool2_3Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Try to use the tool on two dice
     */
    @Test
    public void twoDieMove(){
        try{
            assertTrue(tool2_3Handler.process(new PlayerMove(Tool.MOSSASTANDARD,1,0,0),remoteView,model));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        model.setNormalMove(false);
        model.setUsedTool(false);
        try{
            assertTrue(tool2_3Handler.process(new PlayerMove(Tool.MOSSASTANDARD,2,0,0),remoteView,model));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        playerMove = new PlayerMove(Tool.PENNELLOPEREGLOMISE,1,0,2,1);
        try{
            assertTrue(tool2_3Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Try to use the tool on two dice with invalid parameters
     */
    @Test
    public void twoDieInvalidMove(){
        try{
            assertTrue(tool2_3Handler.process(new PlayerMove(Tool.MOSSASTANDARD,1,0,0),remoteView,model));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        model.setNormalMove(false);
        model.setUsedTool(false);
        try{
            assertTrue(tool2_3Handler.process(new PlayerMove(Tool.MOSSASTANDARD,2,0,0),remoteView,model));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        playerMove = new PlayerMove(Tool.PENNELLOPEREGLOMISE,1,0,0,4);
        try{
            assertFalse(tool2_3Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
