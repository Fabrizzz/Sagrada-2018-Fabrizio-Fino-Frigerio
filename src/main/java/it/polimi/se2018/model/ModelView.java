package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.Tool;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ModelView implements Observer, Serializable {

    private final List<Tool> tools;
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
        usedTool = model.HasUsedTool();
        normalMove = model.HasUsedNormalMove();


        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        Model model = (Model) o;
        round = model.getRound();
        firstTurn = model.isFirstTurn();
        usedTool = model.HasUsedTool();
        normalMove = model.HasUsedNormalMove();

    }

    public int getRound() {
        return round;
    }

    public boolean isFirstTurn() {
        return firstTurn;
    }

    public boolean isUsedTool() {
        return usedTool;
    }

    public boolean isNormalMove() {
        return normalMove;
    }

    private PlayerBoard getBoard(Player player) {
        return boardMap.get(player);
    }

    private DiceBag getDiceBag() {
        return diceBag;
    }

    private DraftPool getDraftPool() {
        return draftPool;
    }

    private RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public boolean boardContainsDie(Player player, int row, int column) {
        return getBoard(player).containsDie(row, column);
    }

    public Die getBoardDie(Player player, int row, int column) throws NoDieException {

        return getBoard(player).getDie(row, column);
    }

    public Die getRoundTrackDie(int round, int pos) {
        return getRoundTrack().getDie(round, pos);
    }

    public boolean roundTrackContainsColor(Color color) {
        return getRoundTrack().hasColor(color);
    }

    public int DraftPoolSize() {
        return getDraftPool().size();
    }

    public Die getDraftPoolDie(int pos) throws NoDieException {
        return getDraftPool().getDie(pos);
    }


    public Die getDiceBagDie() {
        return getDiceBag().takeDie();
    }

    public List<PublicObjective> getPublicObjective() {
        return publicObjective;
    }

}
