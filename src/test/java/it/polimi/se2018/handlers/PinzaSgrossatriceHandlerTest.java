package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.PinzaSgrossatriceHandler;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.Player;
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

/**
 * @author Alessio
 */
public class PinzaSgrossatriceHandlerTest {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private PinzaSgrossatriceHandler pinzaSgrossatriceHandler;

    /**
     * Initialize the model, the handler and the draftpool for the tests
     */
    @Before
    public void initialize() {

        LOGGER.setLevel(Level.OFF);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize(Tool.PINZASGROSSATRICE);

        playerMove = new PlayerMove(Tool.PINZASGROSSATRICE,0,true);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0), connection);

        model.getDraftPool().removeAll();
        Die die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.ONE);
        model.getDraftPool().addDie(die);
        pinzaSgrossatriceHandler = new PinzaSgrossatriceHandler();
        pinzaSgrossatriceHandler.setNextHandler(new EndOfTheChainHandler());
    }

    /**
     * Try to use an tool not present in the chain of responsibilities
     */
    @Test
    public void notPinzaSgrossatrice(){
        playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,0,0);
        try{
            assertFalse(pinzaSgrossatriceHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

    }

    /**
     * Try to use the tool
     */
    @Test
    public void normalUse(){
        try{
            assertTrue(pinzaSgrossatriceHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        try{
            assertEquals(model.getDraftPool().getDie(0).getNumber(),NumberEnum.TWO);
        }catch (Exception e){
            fail();
        }

        try{
            assertFalse(pinzaSgrossatriceHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        model.setUsedTool(false);
        playerMove = new PlayerMove(Tool.PINZASGROSSATRICE,0,false);
        try{
            assertTrue(pinzaSgrossatriceHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
        try{
            assertEquals(model.getDraftPool().getDie(0).getNumber(),NumberEnum.ONE);
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Try to decrease the number of a die having value ONE
     */
    @Test
    public void decreaseOneTest(){
        playerMove = new PlayerMove(Tool.PINZASGROSSATRICE,0,false);
        try{
            assertFalse(pinzaSgrossatriceHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        try{
            assertEquals(model.getDraftPool().getDie(0).getNumber(),NumberEnum.ONE);
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Try to  increase the number of a die having value SIX
     */
    @Test
    public void increaseSixTest(){
        model.getDraftPool().removeAll();
        Die die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.SIX);
        model.getDraftPool().addDie(die);

        playerMove = new PlayerMove(Tool.PINZASGROSSATRICE,0,true);
        try{
            assertFalse(pinzaSgrossatriceHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        try{
            assertEquals(model.getDraftPool().getDie(0).getNumber(),NumberEnum.SIX);
        }catch (Exception e){
            fail();
        }
    }
}
