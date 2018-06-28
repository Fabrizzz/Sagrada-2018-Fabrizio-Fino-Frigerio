package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.FirstCheck;
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

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

public class FirstCheckTest {
    private RemoteView remoteView;
    private Model model;

    @Before
    public void initialize(){
        Connection connection;
        model = ModelControllerInitializerTest.initialize();
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
    }

    @Test
    public void notYourTurnTest(){
        PlayerMove playerMove = new PlayerMove(Tool.SKIPTURN);
        model.getPlayers().get(0).setYourTurn(false);
        FirstCheck firstCheck = new FirstCheck();
        try{
            assertFalse(firstCheck.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void validParameterTest(){
        PlayerMove playerMove = new PlayerMove(Tool.SKIPTURN);
        FirstCheck firstCheck = new FirstCheck();
        firstCheck.setNextHandler(new EndOfTheChainHandler());
        try{
            firstCheck.process(playerMove,remoteView,model);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void invalidParameterTest(){
        PlayerMove playerMove;
        FirstCheck firstCheck = new FirstCheck();
        firstCheck.setNextHandler(new EndOfTheChainHandler());

        playerMove = new PlayerMove(Tool.MOSSASTANDARD,4,1,1);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.MOSSASTANDARD,-1,1,1);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,5,1);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,-1,1);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,1,122);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,1,-1);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}


        playerMove = new PlayerMove(Tool.TAGLIERINAMANUALE,1,1,4,1);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.TAGLIERINAMANUALE,1,1,-1,1);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.TAGLIERINAMANUALE,1,1,1,5);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

        playerMove = new PlayerMove(Tool.TAGLIERINAMANUALE,1,1,1,-1);
        try{
            firstCheck.process(playerMove,remoteView,model);
            fail();
        }catch (Exception e){}

    }

}
