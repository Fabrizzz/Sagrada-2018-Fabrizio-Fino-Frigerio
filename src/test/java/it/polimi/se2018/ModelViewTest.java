package it.polimi.se2018;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class ModelViewTest {
    private ModelView modelView;
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

        model = new Model(players, publicObjectives, boardMap, privateObjectiveMap, Tool.getRandTools(3));
        model.setRound(3);
        modelView = new ModelView(model);
    }

    @Test
    public void getRound(){
        assertEquals(model.getRound(),modelView.getRound());
    }

    @Test
    public void isFirstTurnTest(){
        assertEquals(model.isFirstTurn(),modelView.isFirstTurn());
    }

    @Test
    public void isUsedToolTest(){
        assertEquals(model.hasUsedTool(),modelView.isUsedTool());
    }

    @Test
    public void isNormalMoveTest(){
        assertEquals(model.hasUsedNormalMove(),modelView.isNormalMove());
    }

    @Test
    public void getBoardTest(){
        for(int i = 0; i < players.size();i++){
            assertEquals(model.getBoard(players.get(i)),modelView.getBoard(players.get(i)));
        }
    }

    @Test
    public void getDiceBagTest(){
        assertEquals(model.getDiceBag(),modelView.getDiceBag());
    }

    @Test
    public void getDraftPoolTest(){
        assertEquals(model.getDraftPool(),modelView.getDraftPool());
    }

    @Test
    public void getRoundTrackTest(){
        assertEquals(model.getRoundTrack(),modelView.getRoundTrack());
    }

    @Test
    public void getPlayerViewTest(){
        assertEquals(model.getPlayers(),modelView.getPlayers());
    }

    @Test
    public void getPlayerTest(){
        assertEquals(players.get(1),modelView.getPlayer(players.get(1).getId()));
    }

    @Test
    public void getToolsTest(){
        assertEquals(model.getTools(),modelView.getTools());
    }

    @Test
    public void boardContainsDieTest(){
        assertEquals(false,modelView.boardContainsDie(modelView.getPlayers().get(1),1,1));
        try{
           model.getBoard(modelView.getPlayers().get(1)).setDie(new Die(Color.BLUE),1,1);
        }catch (AlredySetDie e){
           fail();
        }

        modelView.update(model,model);
        assertEquals(true,modelView.boardContainsDie(modelView.getPlayers().get(1),1,1));
    }

    @Test
    public void getBoardDieTest(){
        try{
            modelView.getBoardDie(modelView.getPlayers().get(1),1,1);
            fail();
        }catch (NoDieException e){}

        Die die = new Die(Color.BLUE);
        try{

            model.getBoard(modelView.getPlayers().get(1)).setDie(die,1,1);
        }catch (AlredySetDie e){
            fail();
        }

        modelView.update(model,model);

        try{
           assertEquals(die,modelView.getBoardDie(modelView.getPlayers().get(1),1,1));
        }catch (NoDieException e){
            fail();
        }
    }

    @Test
    public void getRoundTrackDieTest(){
        try{
            modelView.getRoundTrackDie(1,0);
            fail();
        }catch (NoDieException e){}

        Die die = new Die(Color.BLUE);
        model.getRoundTrack().addDie(1,die);
        modelView.update(model,model);

        try{
            assertEquals(die, modelView.getRoundTrackDie(1,0));
        }catch (NoDieException e){
            fail();
        }
    }

    @Test
    public void roundTrackCOntainsColorTest(){
        assertEquals(false,modelView.roundTrackContainsColor(Color.BLUE));

        Die die = new Die(Color.BLUE);
        model.getRoundTrack().addDie(1,die);
        modelView.update(model,model);

        assertEquals(true,modelView.roundTrackContainsColor(Color.BLUE));
    }

    @Test
    public void draftPoolSizeTest(){
        assertEquals(model.getDraftPool().size(),modelView.DraftPoolSize());
    }

    @Test
    public void getDraftPoolDieTest(){
        try{
            assertEquals(model.getDraftPool().getDie(1),modelView.getDraftPoolDie(1));
        }catch (NoDieException e){
            fail();
        }

        try{
            modelView.getDraftPoolDie(100);
            fail();
        }catch (NoDieException e){}

    }

    @Test
    public void getDiceBagDie(){
        assertEquals(model.getDiceBag().getFirst(),modelView.getDiceBagDie());
    }

    @Test
    public void getPublicObjectivesTest(){
        assertEquals(model.getPublicObjectives(),modelView.getPublicObjective());
    }

}
