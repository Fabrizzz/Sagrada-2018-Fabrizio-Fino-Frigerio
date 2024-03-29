package it.polimi.se2018;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.utils.ModelControllerInitializerTest;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlreadySetDie;
import it.polimi.se2018.utils.exceptions.EmptyBagException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertTrue;

/**
 * @author Alessio
 */

public class TestModelView {
    private ModelView modelView;
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private  Model model;
    private ArrayList<Player> players = new ArrayList<>();
    private List<PublicObjective> publicObjectives;
    private  PlayerBoard[] playerBoard;
    private Map<Player, PlayerBoard> boardMap;
    private  PrivateObjective[] privateObjective;
    private  Map<Player, PrivateObjective> privateObjectiveMap;
    private BoardList BoardList = new BoardList();

    /**
     * initializing the modelview
     */
    @Before
    public void initialize(){
        LOGGER.setLevel(Level.OFF);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.FINEST);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        model = ModelControllerInitializerTest.initialize();
        players = (ArrayList) model.getPlayers();
        model.setRound(3);
        modelView = new ModelView(model);
    }

    /**
     * testing the costructor of the modelview
     */
    @Test
    public void initialize2Test(){
        modelView = new ModelView(ModelControllerInitializerTest.initialize(Tool.PENNELLOPERPASTASALDA));
    }

    /**
     * testing getting the round
     */
    @Test
    public void getRound(){
        assertEquals(model.getRound(),modelView.getRound());
    }

    /**
     * testing getting the first turn status
     */
    @Test
    public void isFirstTurnTest(){
        assertEquals(model.isFirstTurn(),modelView.isFirstTurn());
    }

    /**
     * testing getting the used tool status
     */
    @Test
    public void isUsedToolTest(){
        assertEquals(model.hasUsedTool(),modelView.isUsedTool());
    }

    /**
     * test getting the normal move status
     */
    @Test
    public void isNormalMoveTest(){
        assertEquals(model.hasUsedNormalMove(),modelView.isNormalMove());
    }

    /**
     * testing getting a board
     */
    @Test
    public void getBoardTest(){
        for(int i = 0; i < players.size();i++){
            assertEquals(model.getBoard(players.get(i)),modelView.getBoard(players.get(i)));
        }
    }

    /**
     * testing getting the dicebag
     */
    @Test
    public void getDiceBagTest(){
        assertEquals(model.getDiceBag(),modelView.getDiceBag());
    }

    /**
     * testing getting the dicepool
     */
    @Test
    public void getDraftPoolTest(){
        assertEquals(model.getDraftPool(),modelView.getDraftPool());
    }

    /**
     * testing getting the roundtrack
     */
    @Test
    public void getRoundTrackTest(){
        assertEquals(model.getRoundTrack(),modelView.getRoundTrack());
    }

    /**
     * testing getting the players
     */
    @Test
    public void getPlayerViewTest(){
        assertEquals(model.getPlayers(),modelView.getPlayers());
    }

    /**
     *test getting a player
     */
    @Test
    public void getPlayerTest(){
        assertEquals(players.get(1),modelView.getPlayer(players.get(1).getId()));
    }

    /**
     * test getting the tools
     */
    @Test
    public void getToolsTest(){
        assertEquals(model.getTools(),modelView.getTools());
    }

    /**
     * test if a board contains a die
     */
    @Test
    public void boardContainsDieTest(){
        assertEquals(false,modelView.boardContainsDie(modelView.getPlayers().get(1),1,1));
        try{
           model.getBoard(modelView.getPlayers().get(1)).setDie(new Die(Color.BLUE),1,1);
        } catch (AlreadySetDie e) {
           fail();
        }

        //modelView.update(model,model);
        assertTrue(modelView.boardContainsDie(modelView.getPlayers().get(1),1,1));
    }

    /**
     * test getting a die from a board
     */
    @Test
    public void getBoardDieTest(){
        try{
            modelView.getBoardDie(modelView.getPlayers().get(1),1,1);
            fail();
        }catch (NoDieException e){}

        Die die = new Die(Color.BLUE);
        try{

            model.getBoard(modelView.getPlayers().get(1)).setDie(die,1,1);
        } catch (AlreadySetDie e) {
            fail();
        }

        //modelView.update(model,model);

        try{
           assertEquals(die,modelView.getBoardDie(modelView.getPlayers().get(1),1,1));
        }catch (NoDieException e){
            fail();
        }
    }

    /**
     * test getting a die from the roundtrack
     */
    @Test
    public void getRoundTrackDieTest(){
        try{
            modelView.getRoundTrackDie(1,0);
            fail();
        }catch (NoDieException e){}

        Die die = new Die(Color.BLUE);
        model.getRoundTrack().addDie(1,die);
        //modelView.update(model,model);

        try{
            assertEquals(die, modelView.getRoundTrackDie(1,0));
        }catch (NoDieException e){
            fail();
        }
    }

    /**
     * test if the roundtrack contains a color
     */
    @Test
    public void roundTrackCOntainsColorTest(){
        assertFalse(modelView.roundTrackContainsColor(Color.BLUE));

        Die die = new Die(Color.BLUE);
        model.getRoundTrack().addDie(1,die);
        //modelView.update(model,model);

        assertTrue(modelView.roundTrackContainsColor(Color.BLUE));
    }

    /**
     * test the draftpool size
     */
    @Test
    public void draftPoolSizeTest(){
        assertEquals(model.getDraftPool().size(),modelView.DraftPoolSize());
    }

    /**
     * test getting a die from the draftpool
     */
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

    /**
     * testing getting a die from the dicebag
     */
    @Test
    public void getDiceBagDie(){
        try {
            assertEquals(model.getDiceBag().getFirst(),modelView.getDiceBagDie());
        } catch (EmptyBagException e) {
            fail();
        }
    }

    /**
     * test getting the publicobjectives
     */
    @Test
    public void getPublicObjectivesTest(){
        assertEquals(model.getPublicObjectives(),modelView.getPublicObjective());
    }

}
