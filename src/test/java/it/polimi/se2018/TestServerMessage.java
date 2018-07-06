package it.polimi.se2018;

import it.polimi.se2018.model.*;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.MessageType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.messages.ServerMessage;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Alessio
 */
public class TestServerMessage {
    private ModelView modelView;
    private  Model model;
    private ArrayList<Player> players = new ArrayList<>();
    private List<PublicObjective> publicObjectives;
    private  PlayerBoard[] playerBoard;
    private Map<Player, PlayerBoard> boardMap;
    private  PrivateObjective[] privateObjective;
    private  Map<Player, PrivateObjective> privateObjectiveMap;
    private BoardList BoardList = new BoardList();

    @Test
    public void getErrorTypeTest(){
        ServerMessage serverMessage = new ServerMessage(ErrorType.ILLEGALMOVE);
        assertEquals(ErrorType.ILLEGALMOVE,serverMessage.getErrorType());
    }

    @Test
    public void getModelViewTest(){
        BoardList boardList = new BoardList();
        boardMap = new HashMap();
        boardMap.put(new Player("asd",(long)123),new PlayerBoard(boardList.getCouple()[1]));
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

        List<Tool> tools = Tool.getRandTools(3);

        this.model = new Model(new ArrayList<>(boardMap.keySet()), publicObjectives, boardMap, privateObjectiveMap, tools);
        Collections.addAll(this.players,boardMap.keySet().toArray(new Player[boardMap.keySet().size()]));
        this.playerBoard = boardMap.values().toArray(new PlayerBoard[boardMap.values().size()]);
        this.privateObjective = privateObjectiveMap.values().toArray(new PrivateObjective[privateObjectiveMap.values().size()]);
        model.setRound(3);
        modelView = new ModelView(model);

        ServerMessage serverMessage = new ServerMessage(modelView);
        assertEquals(modelView,serverMessage.getModelView());
        assertEquals(MessageType.INITIALCONFIGSERVER, serverMessage.getMessageType());
    }

}
