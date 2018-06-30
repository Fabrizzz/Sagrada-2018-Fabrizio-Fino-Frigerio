package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.MartellettoHandler;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.TestConnection;
import jdk.nashorn.internal.runtime.ECMAException;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

/**
 * @author Alessio
 */
public class TestMartellettoHandler {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private MartellettoHandler martellettoHandler;

    /**
     * Intialize the model and the handler for the tests
     */
    @Before
    public void initialize(){
        LOGGER.setLevel(Level.OFF);

        model = ModelControllerInitializerTest.initialize(Tool.MARTELLETTO);

        playerMove = new PlayerMove(Tool.MARTELLETTO);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        martellettoHandler = new MartellettoHandler();
        martellettoHandler.setNextHandler(new EndOfTheChainHandler());

    }

    /**
     * Test the tool in the first and second turn
     */
    @Test
    public void turnCheckTest(){
        model.setFirstTurn(true);
        try{
            assertFalse(martellettoHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
        model.setFirstTurn(false);

        model.setNormalMove(true);
        try{
            assertFalse(martellettoHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
        model.setNormalMove(false);


        try{
            assertTrue(martellettoHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        try{
            assertFalse(martellettoHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Try to use a tool not present in the tool list for the match
     */
    @Test
    public void notMartellettoTest(){
        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,1,1,1);

        try{
            assertFalse(martellettoHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }
}
