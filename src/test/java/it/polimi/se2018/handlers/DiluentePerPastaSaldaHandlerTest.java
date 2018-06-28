package it.polimi.se2018.handlers;

import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.DiluentePerPastaSaldaHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTheChainHandler;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.cell.Die;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DiluentePerPastaSaldaHandlerTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;
    private Connection connection;

    @Before
    public void initialize(){

        LOGGER.setLevel(Level.FINEST);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize(Tool.DILUENTEPERPASTASALDA);

        playerMove = new PlayerMove(1,0,0,NumberEnum.ONE,Tool.DILUENTEPERPASTASALDA);
        connection = new TestConnection();
        remoteView = new RemoteView(model.getPlayers().get(0),connection);
    }

    @Test
    public void testPiazzamentoAVuoto(){
        DiluentePerPastaSaldaHandler diluentePerPastaSaldaHandler = new DiluentePerPastaSaldaHandler();

        try{
            assertTrue(diluentePerPastaSaldaHandler.process(playerMove,remoteView,model));
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testPiazzamentoConDado(){
        Die bagDie;
        try{
            bagDie = model.getDiceBag().getFirst();

            Die die;
            if(bagDie.getColor().equals(Color.BLUE)){
                die = new Die(Color.RED);
            }else{
                die = new Die(Color.BLUE);
            }

            die.setNumber(NumberEnum.TWO);
            try{
                model.getBoard(model.getPlayers().get(0)).setDie(die,2,0);
            }catch (Exception e){
                fail();
            }

            DiluentePerPastaSaldaHandler diluentePerPastaSaldaHandler = new DiluentePerPastaSaldaHandler();
            diluentePerPastaSaldaHandler.setNextHandler(new EndOfTheChainHandler());

            model.setUsedTool(false);
            try{
               assertTrue(diluentePerPastaSaldaHandler.process(playerMove,remoteView,model));
            }catch (Exception e){
                e.printStackTrace();
                fail();
            }

        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void testPiazzamentoSenzaRow(){
        Die bagDie;
        try{
            bagDie = model.getDiceBag().getFirst();

            Die die;
            if(bagDie.getColor().equals(Color.BLUE)){
                die = new Die(Color.RED);
            }else{
                die = new Die(Color.BLUE);
            }

            die.setNumber(NumberEnum.TWO);

            try{
                model.getBoard(model.getPlayers().get(0)).setDie(die,2,0);
            }catch (Exception e){
                fail();
            }

            DiluentePerPastaSaldaHandler diluentePerPastaSaldaHandler = new DiluentePerPastaSaldaHandler();
            diluentePerPastaSaldaHandler.setNextHandler(new EndOfTheChainHandler());
            playerMove = new PlayerMove(0,NumberEnum.ONE,Tool.DILUENTEPERPASTASALDA);
            model.setUsedTool(false);
            try{
                assertFalse(diluentePerPastaSaldaHandler.process(playerMove,remoteView,model));
            }catch (Exception e){
                e.printStackTrace();
                fail();
            }

        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void testPiazzamentoSenzaRowCheckFalse(){
        Die bagDie;
        try{
            Die die = new Die(Color.RED);

            die.setNumber(NumberEnum.FOUR);

            try{
                model.getBoard(model.getPlayers().get(0)).setDie(die,3,4);
            }catch (Exception e){
                fail();
            }

            for(int i = 0; i < model.getDiceBag().size(); i++){
                model.getDiceBag().takeDie();
            }
            model.getDiceBag().addDie(new Die(Color.PURPLE));

            DiluentePerPastaSaldaHandler diluentePerPastaSaldaHandler = new DiluentePerPastaSaldaHandler();
            diluentePerPastaSaldaHandler.setNextHandler(new EndOfTheChainHandler());
            playerMove = new PlayerMove(0,NumberEnum.ONE,Tool.DILUENTEPERPASTASALDA);
            try{
                assertTrue(diluentePerPastaSaldaHandler.process(playerMove,remoteView,model));
            }catch (Exception e){
                e.printStackTrace();
                fail();
            }

        }catch (Exception e){
            fail();
        }
    }
}
