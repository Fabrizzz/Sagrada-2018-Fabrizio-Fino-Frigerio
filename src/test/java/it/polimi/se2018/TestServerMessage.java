package it.polimi.se2018;

import it.polimi.se2018.model.*;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.ServerMessage;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.MessageType;
import it.polimi.se2018.utils.enums.Tool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Alessio
 */
public class TestServerMessage {
    private ModelView modelView;
    private Model model;
    private List<Player> players;
    private List<PublicObjective> publicObjectives;
    private  PlayerBoard[] playerBoard;
    private Map<Player, PlayerBoard> boardMap;
    private  PrivateObjective[] privateObjective;
    private  Map<Player, PrivateObjective> privateObjectiveMap;

    @Test
    public void getErrorTypeTest(){
        ServerMessage serverMessage = new ServerMessage(ErrorType.ILLEGALMOVE);
        assertEquals(ErrorType.ILLEGALMOVE,serverMessage.getErrorType());
    }

    @Test
    public void getModelViewTest(){
        players = new ArrayList<>();

        publicObjectives = new ArrayList<>();
        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        playerBoard = new PlayerBoard[3];
        BoardList.loadJSONBoards();
        for(int j = 0; j < 3; j ++){
            playerBoard[j] = new PlayerBoard(BoardList.getBoard("Virtus"));
        }
        boardMap = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            players.add(new Player("Player " + i, (long) i));
            boardMap.put(players.get(i), playerBoard[i]);
        }
        privateObjective = new PrivateObjective[3];
        privateObjective[0] = new PrivateObjective(Color.BLUE);
        privateObjective[1] = new PrivateObjective(Color.RED);
        privateObjective[2] = new PrivateObjective(Color.YELLOW);
        privateObjectiveMap = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            privateObjectiveMap.put(players.get(i), privateObjective[i]);
        }

        model = new Model(players, publicObjectives, boardMap, privateObjectiveMap, Tool.getRandTools(3));
        model.setRound(3);
        modelView = new ModelView(model);

        ServerMessage serverMessage = new ServerMessage(MessageType.MODELVIEWUPDATE,modelView);
        assertEquals(modelView,serverMessage.getModelView());
        assertEquals(MessageType.MODELVIEWUPDATE,serverMessage.getMessageType());
    }

}
