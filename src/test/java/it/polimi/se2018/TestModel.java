package it.polimi.se2018;

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
import it.polimi.se2018.utils.exceptions.SizeLimitExceededException;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.*;

public class TestModel {
    private  Model model;
    private ArrayList<Player> players = new ArrayList<>();
    private List<PublicObjective> publicObjectives;
    private  PlayerBoard[] playerBoard;
    private Map<Player, PlayerBoard> boardMap;
    private  PrivateObjective[] privateObjective;
    private  Map<Player, PrivateObjective> privateObjectiveMap;
    private BoardList BoardList = new BoardList();

    @Before
    public void initialize(){
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

    }

    @Test
    public void testCostructor(){
        try{
            model = new Model(players, publicObjectives, boardMap, privateObjectiveMap, Tool.getRandTools(3));
        }catch(IllegalArgumentException e){
            fail();
        }

        publicObjectives.add(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        try{
            model = new Model(players, publicObjectives, boardMap, privateObjectiveMap, Tool.getRandTools(3));
            fail();
        }catch(IllegalArgumentException e){}
        publicObjectives.remove(3);

        players.add(new Player("Player " + 3, (long) 3));
        try{
            model = new Model(players, publicObjectives, boardMap, privateObjectiveMap, Tool.getRandTools(3));
            fail();
        }catch(IllegalArgumentException e){}

        PrivateObjective temp = new PrivateObjective(Color.GREEN);

        try{
            privateObjectiveMap.put(players.get(2), temp);
            model = new Model(players, publicObjectives, boardMap, privateObjectiveMap, Tool.getRandTools(3));
            fail();
        }catch(IllegalArgumentException | IndexOutOfBoundsException e){}

    }

    @Test
    public void testSetRound(){
        int roundI = model.getRound();
        model.setRound(1);
        assertEquals(model.getRound(),roundI + 1);
    }

    @Test
    public void testSetFirstTurn(){
        assertTrue(model.isFirstTurn());
        model.setFirstTurn(false);
        assertFalse(model.isFirstTurn());
        model.setFirstTurn(true);
        assertTrue(model.isFirstTurn());
    }

    @Test
    public void testSetUsedTool(){
        assertFalse(model.hasUsedTool());
        model.setUsedTool(true);
        assertTrue(model.hasUsedTool());
        model.setUsedTool(false);
        assertFalse(model.hasUsedTool());
    }

    @Test
    public void testGetBoard(){
        assertSame(boardMap.get(players.get(0)), model.getBoard(players.get(0)));
    }

    @Test
    public void testGetPlayers(){
        assertEquals(players, model.getPlayers());
    }


    @Test
    public void testGetPublicObjectives(){
        assertEquals(publicObjectives,model.getPublicObjectives());
    }

    @Test
    public void testSetPublicObjective(){
        try{
            model.setPublicObjective(PublicObjectiveFactory.createPublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
            fail();
        }catch(SizeLimitExceededException e){}
    }


    @Test
    public void setNormalMoveTest(){
        assertFalse(model.hasUsedNormalMove());
        model.setNormalMove(true);
        assertTrue(model.hasUsedNormalMove());
    }
}
