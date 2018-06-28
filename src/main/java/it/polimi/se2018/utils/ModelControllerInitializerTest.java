package it.polimi.se2018.utils;

import it.polimi.se2018.model.BoardList;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.Tool;

import java.util.*;


public class ModelControllerInitializerTest {

    public static Model initialize(Tool tool) {
        Model model;
        List<PublicObjective> publicObjectives;
        Map<Player, PlayerBoard> boardMap;
        Map<Player, PrivateObjective> privateObjectiveMap;
        BoardList boardList = new BoardList();

        boardMap = new HashMap();
        Player player = new Player("asd", (long) 123);
        boardMap.put(player, new PlayerBoard(boardList.getBoard("Firelight")));
        boardMap.put(new Player("asd", (long) 1234), new PlayerBoard(boardList.getBoard("Firelight")));
        publicObjectives = new ArrayList<>();
        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.COLORIDIVERSICOLONNA));
        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.DIAGONALICOLORATE));

        List colors = Arrays.asList(Color.values());
        Collections.shuffle(colors);

        privateObjectiveMap = new HashMap<>();
        privateObjectiveMap.put(new Player("asd", (long) 123), new PrivateObjective(Color.BLUE));
        privateObjectiveMap.put(new Player("assd", (long) 321), new PrivateObjective(Color.RED));

        List<Tool> tools = new ArrayList<>();
        tools.add(Tool.SKIPTURN);
        tools.add(Tool.MOSSASTANDARD);
        tools.add(tool);

        for (Player tmpPlayer : boardMap.keySet()) {
            tmpPlayer.setFavorTokens(boardMap.get(player).getBoardDifficulty());
        }

        model = new Model(new ArrayList<>(boardMap.keySet()), publicObjectives, boardMap, privateObjectiveMap, tools);

        return model;
    }

    public static Model initialize(){
        return initialize(Tool.MARTELLETTO);
    }
}
