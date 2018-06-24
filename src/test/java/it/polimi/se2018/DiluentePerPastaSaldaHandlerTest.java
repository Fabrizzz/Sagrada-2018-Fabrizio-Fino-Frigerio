package it.polimi.se2018;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.controller.chainOfResponsibilities.DiluentePerPastaSaldaHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.EndOfTurnHandler;
import it.polimi.se2018.controller.chainOfResponsibilities.TestHandler;
import it.polimi.se2018.model.BoardList;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.network.TestConnection;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DiluentePerPastaSaldaHandlerTest {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private Model model;

    @Before
    public void initialize(){

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.FINEST);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize();

        playerMove = new PlayerMove(1,0,0,NumberEnum.ONE,Tool.DILUENTEPERPASTASALDA);

        remoteView = new RemoteView(model.getPlayers().get(0),new TestConnection());
    }

    @Test
    public void testPiazzamentoAVuoto(){
        DiluentePerPastaSaldaHandler diluentePerPastaSaldaHandler = new DiluentePerPastaSaldaHandler();
        diluentePerPastaSaldaHandler.setNextHandler(new TestHandler());

        try{
            diluentePerPastaSaldaHandler.process(playerMove,remoteView,model);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }



}
