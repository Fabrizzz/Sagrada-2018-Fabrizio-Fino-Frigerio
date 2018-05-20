package it.polimi.se2018;

import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.exceptions.SizeLimitExceededException;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.*;

public class TestModel {
    private  Model model;
    private Player[] players;
    private List<PublicObjective> publicObjectives;
    private  PlayerBoard[] playerBoard;
    private Map<Player, PlayerBoard> boardMap;
    private  PrivateObjective[] privateObjective;
    private  Map<Player, PrivateObjective> privateObjectiveMap;

    @Before
    public void initialize(){
        players = new Player[3];

        publicObjectives = new ArrayList<>();
        publicObjectives.add(new PublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        publicObjectives.add(new PublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        publicObjectives.add(new PublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        playerBoard = new PlayerBoard[3];
        boardMap = new HashMap<Player,PlayerBoard>();
        for(int i = 0; i < players.length; i ++){
            players[i] = new Player("Player " + i);
            boardMap.put(players[i],playerBoard[i]);
        }
        privateObjective = new PrivateObjective[3];
        privateObjective[0] = new PrivateObjective(Color.BLUE);
        privateObjective[1] = new PrivateObjective(Color.RED);
        privateObjective[2] = new PrivateObjective(Color.YELLOW);
        privateObjectiveMap = new HashMap<Player,PrivateObjective>();
        for(int i = 0; i < players.length; i ++){
            privateObjectiveMap.put(players[i],privateObjective[i]);
        }
        assertEquals(players.length,boardMap.size());
        model = new Model(players, publicObjectives,boardMap,privateObjectiveMap);

    }

    @Test
    public void testCostructor(){
        try{
            model = new Model(players, publicObjectives,boardMap,privateObjectiveMap);
        }catch(IllegalArgumentException e){
            fail();
        }

        publicObjectives.add(new PublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
        try{
            model = new Model(players, publicObjectives,boardMap,privateObjectiveMap);
            fail();
        }catch(IllegalArgumentException e){}
        publicObjectives.remove(3);

        players = new Player[4];
        for(int i = 0; i < players.length; i ++){
            players[i] = new Player("Player " + i);
        }
        try{
            model = new Model(players, publicObjectives,boardMap,privateObjectiveMap);
            fail();
        }catch(IllegalArgumentException e){}

        privateObjective = new PrivateObjective[4];
        privateObjective[0] = new PrivateObjective(Color.BLUE);
        privateObjective[1] = new PrivateObjective(Color.RED);
        privateObjective[2] = new PrivateObjective(Color.YELLOW);
        privateObjective[3] = new PrivateObjective(Color.GREEN);
        privateObjectiveMap.put(players[3],privateObjective[3]);
        try{
            model = new Model(players, publicObjectives,boardMap,privateObjectiveMap);
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
        assertEquals(boardMap.get(players[0]),model.getBoard(players[0]));
        assertSame(boardMap.get(players[1]),model.getBoard(players[0]));
    }

    @Test
    public void testGetPlayers(){
        assertEquals( new ArrayList<>(Arrays.asList(players)),model.getPlayers());
    }

    @Test
    public void testGetPrivateObjective(){
        for(int i = 0; i < players.length; i ++) {
            try{
                assertEquals(privateObjective[i], model.getPrivateObjective(players[i]));
            }catch (IllegalArgumentException e){
                fail();
            }
        }

        privateObjective = new PrivateObjective[4];
        privateObjective[0] = new PrivateObjective(Color.BLUE);
        privateObjective[1] = new PrivateObjective(Color.RED);
        privateObjective[2] = new PrivateObjective(Color.YELLOW);
        privateObjective[3] = new PrivateObjective(Color.GREEN);
        players = new Player[4];
        for(int i = 0; i < players.length; i ++){
            players[i] = new Player("Player " + i);
        }
        try{
            assertEquals(privateObjective[3], model.getPrivateObjective(players[3]));
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
            model.setPublicObjective(new PublicObjective(PublicObjectiveName.COLORIDIVERSIRIGA));
            fail();
        }catch(SizeLimitExceededException e){}
    }

    @Test
    public void testEndRound(){
        int round = model.getRound();
        DraftPool draftPool = model.getDraftPool();
        model.endRound();
        assertEquals(round + 1,model.getRound());
        assertTrue(model.isFirstTurn());
        //da completare
    }

    @Test
    public void testNextTurn(){
        model.setUsedTool(true);
        model.nextTurn();
        assertFalse(model.hasUsedTool());
        //da completare

    }
}
