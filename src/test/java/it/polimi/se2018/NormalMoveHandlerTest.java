package it.polimi.se2018;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.NormalMoveHandler;
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

public class NormalMoveHandlerTest {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private NormalMoveHandler normalMoveHandler;

    @Before
    public void initialize(){

        LOGGER.setLevel(Level.FINEST);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize();

        playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,0,0);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        normalMoveHandler = new NormalMoveHandler();
        normalMoveHandler.setNextHandler(new EndOfTheChainHandler());

        for(int i = 0; i < model.getDiceBag().size(); i++){
            model.getDiceBag().takeDie();
        }
    }

    @Test
    public void notMossaStandardTest(){
        playerMove = new PlayerMove(Tool.MARTELLETTO);
        try{
            assertFalse(normalMoveHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void dieTest(){
        Die die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.ONE);
        model.getDiceBag().addDie(die);
        try{
            assertTrue(normalMoveHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        die = new Die(Color.RED);
        die.setNumber(NumberEnum.TWO);
        model.getDiceBag().addDie(die);
        playerMove = new PlayerMove(Tool.MOSSASTANDARD,2,0,0);
        try{
            assertFalse(normalMoveHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
        model.setNormalMove(false);
        playerMove = new PlayerMove(Tool.MOSSASTANDARD,2,0,0);
        try{
            assertTrue(normalMoveHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }
}
