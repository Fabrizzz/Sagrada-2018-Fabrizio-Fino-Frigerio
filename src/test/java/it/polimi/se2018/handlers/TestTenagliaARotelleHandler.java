package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.NormalMoveHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.TaglierinaCircolareHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.TenagliaARotelleHandler;
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

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * @author Alessio
 */
public class TestTenagliaARotelleHandler {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private TenagliaARotelleHandler tenagliaARotelleHandler;

    /**
     * Initialize the model, the handler and the draftpool for the test
     */
    @Before
    public void initialize(){

        LOGGER.setLevel(Level.OFF);

        model = ModelControllerInitializerTest.initialize(Tool.TENAGLIAAROTELLE);

        playerMove = new PlayerMove(Tool.TENAGLIAAROTELLE);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        tenagliaARotelleHandler = new TenagliaARotelleHandler();
        NormalMoveHandler normalMoveHandler = new NormalMoveHandler();
        tenagliaARotelleHandler.setNextHandler(normalMoveHandler);
        normalMoveHandler.setNextHandler(new EndOfTheChainHandler());

        model.getDraftPool().removeAll();
        Die die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.ONE);
        model.getDraftPool().addDie(die);
        die = new Die(Color.RED);
        die.setNumber(NumberEnum.SIX);
        model.getDraftPool().addDie(die);
        die = new Die(Color.YELLOW);
        die.setNumber(NumberEnum.FOUR);
        model.getDraftPool().addDie(die);
    }

    /**
     * Try to use an tool not present in the chain of responsibilities
     */
    @Test
    public void notTenagliaARotelle(){
        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,0,0,0);
        try{
            assertFalse(tenagliaARotelleHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Try to use the tool after a normalMove
     */
    @Test
    public void afterNormalMoveTest(){
        assertFalse(model.getPlayers().get(0).isCanDoTwoTurn());

        try{
            assertTrue(tenagliaARotelleHandler.process(new PlayerMove(Tool.MOSSASTANDARD,1,0,0),remoteView,model));
        }catch (Exception e){
            fail();
        }
        assertTrue(model.hasUsedNormalMove());

        try{
            assertTrue(tenagliaARotelleHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        assertFalse(model.getPlayers().get(0).isCanDoTwoTurn());
        assertFalse(model.hasUsedNormalMove());
        try{
            assertTrue(tenagliaARotelleHandler.process(new PlayerMove(Tool.MOSSASTANDARD,2,0,0),remoteView,model));
        }catch (Exception e){
            fail();
        }
        assertTrue(model.hasUsedNormalMove());
        assertFalse(model.getPlayers().get(0).isCanDoTwoTurn());

        try{
            assertFalse(tenagliaARotelleHandler.process(new PlayerMove(Tool.MOSSASTANDARD,2,1,0),remoteView,model));
        }catch (Exception e){
            fail();
        }

    }

    /**
     * Try to use the tool before a normal move
     */
    @Test
    public void preNormalMoveTest(){
        assertFalse(model.getPlayers().get(0).isCanDoTwoTurn());

        try{
            assertTrue(tenagliaARotelleHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        assertTrue(model.getPlayers().get(0).isCanDoTwoTurn());
        assertFalse(model.hasUsedNormalMove());

        try{
            assertTrue(tenagliaARotelleHandler.process(new PlayerMove(Tool.MOSSASTANDARD,1,0,0),remoteView,model));
        }catch (Exception e){
            fail();
        }
        assertFalse(model.hasUsedNormalMove());
        assertFalse(model.getPlayers().get(0).isCanDoTwoTurn());

        try{
            assertTrue(tenagliaARotelleHandler.process(new PlayerMove(Tool.MOSSASTANDARD,2,0,0),remoteView,model));
        }catch (Exception e){
            fail();
        }

        assertTrue(model.hasUsedNormalMove());
        assertFalse(model.getPlayers().get(0).isCanDoTwoTurn());

    }

    /**
     * Try to use the tool during the second turn
     */
    @Test
    public void notFirstTurnTest(){
        model.setFirstTurn(false);
        try{
            assertFalse(tenagliaARotelleHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }
}
