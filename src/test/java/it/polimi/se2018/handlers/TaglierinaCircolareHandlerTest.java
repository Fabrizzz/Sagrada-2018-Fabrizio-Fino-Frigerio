package it.polimi.se2018.handlers;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.RigaInSugheroHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.TaglierinaCircolareHandler;
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

public class TaglierinaCircolareHandlerTest {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;
    private TaglierinaCircolareHandler taglierinaCircolareHandler;

    @Before
    public void initialize(){

        LOGGER.setLevel(Level.FINEST);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize(Tool.TAGLIERINACIRCOLARE);

        playerMove = new PlayerMove(0,0,0,Tool.TAGLIERINACIRCOLARE);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
        taglierinaCircolareHandler = new TaglierinaCircolareHandler();
        taglierinaCircolareHandler.setNextHandler(new EndOfTheChainHandler());

        Die die = new Die(Color.BLUE);
        die.setNumber(NumberEnum.SIX);
        model.getRoundTrack().addDie(0,die);

        model.getDraftPool().removeAll();
        die = new Die(Color.RED);
        die.setNumber(NumberEnum.ONE);
        model.getDraftPool().addDie(die);
    }

    @Test
    public void notTaglierinaCircolareTest(){
        playerMove = new PlayerMove(Tool.MOSSASTANDARD,1,0,0);
        try{
            assertFalse(taglierinaCircolareHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void wrongPositionTest(){
        playerMove = new PlayerMove(-1,0,0,Tool.TAGLIERINACIRCOLARE);
        try{
            assertFalse(taglierinaCircolareHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        playerMove = new PlayerMove(0,-1,0,Tool.TAGLIERINACIRCOLARE);
        try{
            assertFalse(taglierinaCircolareHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        playerMove = new PlayerMove(0,0,-1,Tool.TAGLIERINACIRCOLARE);
        try{
            assertFalse(taglierinaCircolareHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        playerMove = new PlayerMove(1,0,0,Tool.TAGLIERINACIRCOLARE);
        try{
            assertFalse(taglierinaCircolareHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        playerMove = new PlayerMove(0,1,0,Tool.TAGLIERINACIRCOLARE);
        try{
            assertFalse(taglierinaCircolareHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        playerMove = new PlayerMove(0,0,1,Tool.TAGLIERINACIRCOLARE);
        try{
            assertFalse(taglierinaCircolareHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void normalTest(){
        try{
            assertTrue(taglierinaCircolareHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        try{
            assertFalse(taglierinaCircolareHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            fail();
        }

        try{
            assertEquals(model.getRoundTrack().getDie(0,0).getNumber(),NumberEnum.ONE );
        }catch (Exception e){
            fail();
        }
        try{
            assertEquals(model.getRoundTrack().getDie(0,0).getColor(),Color.RED );
        }catch (Exception e){
            fail();
        }

        try{
            assertEquals(model.getDraftPool().getDie(0).getNumber(),NumberEnum.SIX );
        }catch (Exception e){
            fail();
        }
        try{
            assertEquals(model.getDraftPool().getDie(0).getColor(),Color.BLUE );
        }catch (Exception e){
            fail();
        }



    }


}
