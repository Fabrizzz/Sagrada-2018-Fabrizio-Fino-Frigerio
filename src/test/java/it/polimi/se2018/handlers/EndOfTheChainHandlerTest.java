package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EndOfTheChainHandlerTest {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private RemoteView remoteView;
    private Model model;
    private Connection connection;

    @Before
    public void initialize(){
        LOGGER.setLevel(Level.OFF);

        model = ModelControllerInitializerTest.initialize();
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
    }

    @Test
    public void testSkipTurn(){
        PlayerMove playerMove = new PlayerMove(Tool.SKIPTURN);
        EndOfTheChainHandler endOfTheChainHandler = new EndOfTheChainHandler();
        try{
            assertTrue(endOfTheChainHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void testNotSkipTurn(){
        PlayerMove playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,1,1);
        EndOfTheChainHandler endOfTheChainHandler = new EndOfTheChainHandler();
        try{
            assertFalse(endOfTheChainHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }
}
