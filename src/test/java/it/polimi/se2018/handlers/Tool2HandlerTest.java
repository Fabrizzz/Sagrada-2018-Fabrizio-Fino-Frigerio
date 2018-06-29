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

public class Tool2HandlerTest {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private Tool2_3Handler tool2_3Handler;

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

        Die die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.SIX);
        model.getRoundTrack().addDie(0,die);

        model.getDraftPool().removeAll();
        die = new Die(Color.RED);
        die.setNumber(NumberEnum.ONE);
        model.getDraftPool().addDie(die);
    }

    @Test
    public void notTool2_3Test(){
        playerMove = new PlayerMove(Tool.RIGAINSUGHERO,1,0,0);
        try{
            assertFalse(tool2_3Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void noDieMoveTest(){
        try{
            assertFalse(tool2_3Handler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    /*@Test
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

    }*/


}
