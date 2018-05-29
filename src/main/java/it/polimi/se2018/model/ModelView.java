package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * ModelView
 * @author Giampietro
 */
public class ModelView implements Observer, Serializable {

    private final Map<Tool, Boolean> tools;
    private final List<PublicObjective> publicObjective;
    private final DiceBag diceBag;    //Il sacchetto contenente i dadi
    private final DraftPool draftPool;  //Dadi pescati del round
    private final RoundTrack roundTrack; //Il tracciato
    private final List<Player> players;   //I player in gioco
    private final Map<Player, PlayerBoard> boardMap;

    private int round;
    private boolean firstTurn;
    private boolean usedTool;
    private boolean normalMove;

    /**
     * Constructor
     * @param model model of the game
     */
    public ModelView(Model model) {

        tools = model.getTools();
        publicObjective = model.getPublicObjectives();
        diceBag = model.getDiceBag();
        draftPool = model.getDraftPool();
        roundTrack = model.getRoundTrack();
        players = model.getPlayers();
        boardMap = model.getBoardMap();
        round = model.getRound();
        firstTurn = model.isFirstTurn();
        usedTool = model.hasUsedTool();
        normalMove = model.hasUsedNormalMove();


        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        Model model = (Model) o;
        round = model.getRound();
        firstTurn = model.isFirstTurn();
        usedTool = model.hasUsedTool();
        normalMove = model.hasUsedNormalMove();

    }

    /**
     * Return the current round number
     * @return current round number
     */
    public int getRound() {
        return round;
    }

    /**
     * Return if is the first turn in the round
     * @return true if is the first turn in the round, false otherwise
     */
    public boolean isFirstTurn() {
        return firstTurn;
    }

    /**
     * Return if a tool card has been used this round
     * @return true if a tool card has been used this round, false otherwise
     */
    public boolean isUsedTool() {
        return usedTool;
    }

    /**
     * Return if a player had done a normal move this round
     * @return true a player had done a normal move this round, false otherwise
     */
    public boolean isNormalMove() {
        return normalMove;
    }

    /**
     * Return the player  board of player
     * @param player player
     * @return playerBoard
     */
    private PlayerBoard getBoard(Player player) {
        return boardMap.get(player);
    }

    /**
     * Return the dicebag
     * @return diceBag
     */
    private DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     * Return the draftPool
     * @return draftPool
     */
    private DraftPool getDraftPool() {
        return draftPool;
    }

    /**
     * Return the roundTrack
     * @return roundTrack
     */
    private RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * Return the list of the players
     * @return list of the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Return the tool card list
     * @return tool card list
     */
    public Map<Tool, Boolean> getTools() {
        return tools;
    }

    /**
     * Return if the cell in position row,column contains a die in the player board
     * @param player player
     * @param row row of the cell
     * @param column column of the cell
     * @return true if the cell contains a die, false otherwise
     */
    public boolean boardContainsDie(Player player, int row, int column) {
        return getBoard(player).containsDie(row, column);
    }

    /**
     * Return the die in the playerBoard in position row,column
     * @param player player
     * @param row row of the cell
     * @param column column of the cell
     * @return die in the cell
     * @throws NoDieException if no die is found in the cell
     */
    public Die getBoardDie(Player player, int row, int column) throws NoDieException {

        return getBoard(player).getDie(row, column);
    }

    /**
     * Return the die in the roundTrack in position pos in round round
     * @param round round
     * @param pos position of the die in the roundTrack
     * @return die in the roundTrack
     */
    public Die getRoundTrackDie(int round, int pos) {
        return getRoundTrack().getDie(round, pos);
    }

    /**
     * Return if the roundTrack contains a die of color color
     * @param color color
     * @return true if the roundTrack contains a die of color color, false otherwise
     */
    public boolean roundTrackContainsColor(Color color) {
        return getRoundTrack().hasColor(color);
    }

    /**
     * Return the size of the DraftPoll
     * @return number of dice in the draftPool
     */
    public int DraftPoolSize() {
        return getDraftPool().size();
    }

    /**
     * Return the die in the draftPool in position pos
     * @param pos position of the die in the draftPool
     * @return the die in the draftPool in position pos
     * @throws NoDieException if no die is found in position pos
     */
    public Die getDraftPoolDie(int pos) throws NoDieException {
        return getDraftPool().getDie(pos);
    }

    /**
     * Draft a die from the dicebag
     * @return Drafted die
     */
    public Die getDiceBagDie() {
        return getDiceBag().takeDie();
    }

    /**
     * Return the list of the public objectives
     * @return list of the public objectives
     */
    public List<PublicObjective> getPublicObjective() {
        return publicObjective;
    }

}
