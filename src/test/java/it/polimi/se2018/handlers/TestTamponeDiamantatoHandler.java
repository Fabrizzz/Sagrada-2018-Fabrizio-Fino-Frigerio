package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.TaglierinaCircolareHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.TamponeDiamantatoHandler;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alessio
 */
public class TestTamponeDiamantatoHandler {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private TamponeDiamantatoHandler tamponeDiamantatoHandler;

    /**
     * Initialize the model, the handler and the draftpool for the test
     */
    @Before
    public void initialize(){

        LOGGER.setLevel(Level.OFF);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize(Tool.TAMPONEDIAMANTATO);

        playerMove = new PlayerMove(Tool.TAMPONEDIAMANTATO,0);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        tamponeDiamantatoHandler = new TamponeDiamantatoHandler();
        tamponeDiamantatoHandler.setNextHandler(new EndOfTheChainHandler());

        model.getDraftPool().removeAll();
        Die die = new Die(Color.RED);
        die.setNumber(NumberEnum.ONE);
        model.getDraftPool().addDie(die);
    }

    /**
     * Try to use an tool not present in the chain of responsibilities
     */
    @Test
    public void notTamponeDiamantato(){
        playerMove = new PlayerMove(Tool.MOSSASTANDARD,0,0,0);
        try{
            assertFalse(tamponeDiamantatoHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Try to use invalid parameters for the tool
     */
    @Test
    public void invalidParameterTest(){
        playerMove = new PlayerMove(Tool.TAMPONEDIAMANTATO,-1);
        try{
            tamponeDiamantatoHandler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        model.setUsedTool(false);
        playerMove = new PlayerMove(Tool.TAMPONEDIAMANTATO,model.getDraftPool().size());
        try{
            tamponeDiamantatoHandler.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

    }

    /**
     * Try to use the tool with valid parameters
     */
    @Test
    public void normalTest(){
        try{
            assertTrue(tamponeDiamantatoHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        try{
            assertFalse(tamponeDiamantatoHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        try{
            assertEquals(model.getDraftPool().getDie(0).getNumber(),NumberEnum.SIX);
        }catch (Exception e ){
            fail();
        }
    }
}
