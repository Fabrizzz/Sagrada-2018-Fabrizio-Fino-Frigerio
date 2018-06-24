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
    private Model model;
    private PlayerMove playerMove;
    private RemoteView remoteView;
    private static final Logger LOGGER = Logger.getLogger("Logger");

    @Before
    public void initialize(){
        List<PublicObjective> publicObjectives;
        Map<Player, PlayerBoard> boardMap;
        Map<Player, PrivateObjective> privateObjectiveMap;
        BoardList boardList = new BoardList();

        boardMap = new HashMap();
        Player player = new Player("asd",(long)123);
        assertNotNull(boardList.getBoard("Bellesguard"));
        assertNotNull(boardList.getBoard("Bellesguard"));
        boardMap.put(player,new PlayerBoard(boardList.getBoard("Bellesguard")));
        boardMap.put(new Player("asd",(long)1234),new PlayerBoard(boardList.getCouple()[1]));

        publicObjectives = new ArrayList<>();
        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.COLORIDIVERSICOLONNA));
        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.DIAGONALICOLORATE));

        List colors = Arrays.asList(Color.values());
        Collections.shuffle(colors);
        Iterator<Color> iterator = colors.iterator();

        privateObjectiveMap = new HashMap<>();
        privateObjectiveMap.put(new Player("asd",(long)123),new PrivateObjective(Color.BLUE));
        privateObjectiveMap.put(new Player("assd",(long)321),new PrivateObjective(Color.RED));

        List<Tool> tools = new ArrayList<>();
        tools.add(Tool.DILUENTEPERPASTASALDA);
        tools.add(Tool.MOSSASTANDARD);
        tools.add(Tool.TAGLIERINAMANUALE);

        for(Player tmpPlayer : boardMap.keySet()){
            tmpPlayer.setFavorTokens(boardMap.get(player).getBoardDifficutly());
        }

        this.model = new Model(new ArrayList<>(boardMap.keySet()), publicObjectives, boardMap, privateObjectiveMap, tools,null);

        playerMove = new PlayerMove(1,0,0,NumberEnum.ONE,Tool.DILUENTEPERPASTASALDA);

        remoteView = new RemoteView(player,new TestConnection());

        LOGGER.setLevel(Level.WARNING);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.FINEST);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);
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
