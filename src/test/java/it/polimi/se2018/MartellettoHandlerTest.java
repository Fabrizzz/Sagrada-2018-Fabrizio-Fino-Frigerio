package it.polimi.se2018;

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

public class MartellettoHandlerTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private MartellettoHandler martellettoHandler;

    @Before
    public void initialize(){

        LOGGER.setLevel(Level.FINEST);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize();

        playerMove = new PlayerMove(Tool.MARTELLETTO);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        martellettoHandler = new MartellettoHandler();
        martellettoHandler.setNextHandler(new EndOfTheChainHandler());

    }

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

    @Test
    public void notMartellettoTest(){
        playerMove = new PlayerMove(Tool.SKIPTURN);

        try{
            assertTrue(martellettoHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }
}
