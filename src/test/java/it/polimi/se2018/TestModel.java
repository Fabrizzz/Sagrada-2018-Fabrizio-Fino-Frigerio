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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.*;

public class TestModel {
    private  Model model;
    private List<Player> players;
    private List<PublicObjective> publicObjectives;
    private  PlayerBoard[] playerBoard;
    private Map<Player, PlayerBoard> boardMap;
    private  PrivateObjective[] privateObjective;
    private  Map<Player, PrivateObjective> privateObjectiveMap;

    @Before
    public void initialize(){
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
        assertEquals(players.size(), boardMap.size());

        model = new Model(players, publicObjectives, boardMap, privateObjectiveMap, Tool.getRandTools(3));

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
        privateObjectiveMap.put(players.get(3), temp);
        try{
            model = new Model(players, publicObjectives, boardMap, privateObjectiveMap, Tool.getRandTools(3));
            fail();
        }catch(IllegalArgumentException e){}

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
    public void testGetPrivateObjective(){
        for (int i = 0; i < players.size(); i++) {
            try{
                assertEquals(privateObjective[i], model.getPrivateObjective(players.get(i)));
            }catch (IllegalArgumentException e){
                fail();
            }
        }

        privateObjective = new PrivateObjective[4];
        privateObjective[0] = new PrivateObjective(Color.BLUE);
        privateObjective[1] = new PrivateObjective(Color.RED);
        privateObjective[2] = new PrivateObjective(Color.YELLOW);
        privateObjective[3] = new PrivateObjective(Color.GREEN);

        players.add(new Player("Player " + 3, (long) 3));
        try{
            assertEquals(privateObjective[3], model.getPrivateObjective(players.get(3)));
            fail();
        }catch(IllegalArgumentException e){}

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

    /*@Test
    public void testEndRound(){
        int round = model.getRound();
        DraftPool draftPool = model.getDraftPool();
        model.endRound();
        assertEquals(round + 1,model.getRound());
        assertTrue(model.isFirstTurn());
        //da completare
    }*/

    @Test
    public void testNextTurn(){
        model.setUsedTool(true);
        model.nextTurn();
        assertFalse(model.hasUsedTool());
        //da completare

    }
}
