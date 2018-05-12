package it.polimi.se2018.model;

import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.utils.Color;
import it.polimi.se2018.utils.Tool;

import java.util.*;

public abstract class Model extends Observable {

    //Define
    private static final int numberOfToolCards = 3;
    private static final int numberOfPublicObjectives = 3;
    private static final int numberOfDicePerColor = 18;     //Nei 90 dadi ho 18 dadi per colore; passo 18 al costruttore del diceBag

    private Map<Player, PlayerBoard> boardMap;
    private Map<Player, PrivateObjective> privateObjectiveMap;

    private int round = 0;
    private boolean firstTurn = true; //ogni round è fatto da due turni, è importante tenerne conto anche per l' uso di certe tool
    private boolean usedTool;
    private boolean normalMove;


    private DiceBag diceBag;    //Il sacchetto contenente i dadi
    private DraftPool draftPool;  //Dadi pescati del round
    private RoundTrack roundTrack; //Il tracciato
    private List<Player> players;   //I player in gioco

    private Tool[] tools;
    private PublicObjective[] publicObjective;


    //Costruttore
    public Model(Player[] players) {
        this.players = new ArrayList<>(Arrays.asList(players)); //Creazione di un arraylist che conterrà i player
        diceBag = new DiceBag(numberOfDicePerColor);    //Creazione del sacchetto; viene passato 18 così verranno generati i 90 dadi, 18 per colore
        draftPool = new DraftPool(players.length, diceBag); //Creazione della variabile draftPool per i dadi pescati; viene passato il numero di dadi da pescare (numplayer  2 +1) e il sacchetto da cui pescare
        tools = Tool.getRandTools(numberOfToolCards);

    }

    public void setBoard(Player player, PlayerBoard playerBoard) {
        boardMap.put(player, playerBoard);
    }

    public boolean verifyNumberRestriction(Player player, Die die, int row, int column) {
        return boardMap.get(player).verifyNumberRestriction(die, row, column);
    }

    public boolean verifyColorRestriction(Player player, Die die, int row, int column) {
        return boardMap.get(player).verifyColorRestriction(die, row, column);
    }

    public boolean verifyPositionRestriction(Player player, int row, int column) {
        return boardMap.get(player).verifyPositionRestriction(row, column);
    }

    public boolean verifyNearCellsRestriction(Player player, Die die, int row, int column) {
        return boardMap.get(player).verifyNearCellsRestriction(die, row, column);
    }

    public boolean verifyInitialPositionRestriction(Player player, int row, int column) {
        return boardMap.get(player).verifyInitialPositionRestriction(row, column);
    }


    public abstract boolean changeDieValue(Player player, int row, int column, int val);

    public abstract boolean containsDie(Player player, int row, int column);

    public abstract Die getDie(Player player, int row, int column);

    public abstract boolean removeDie(Player player, int row, int column);

    public abstract boolean setDie(Player player, Die die, int row, int column);

    public abstract boolean flipDie(Player player, int row, int column);


    public abstract Die putRoundTrackDie(int round);

    public abstract Die getRoundTrackDie(int round, int pos);

    public abstract boolean removeRoundTrackDie(int round, int pos);

    public abstract boolean containsColor(Color color);


    public abstract boolean rollDraftPoolDie(int pos);

    public abstract boolean rollDraftPool();

    public abstract Die getDraftPoolDie(int pos);

    public abstract boolean removeDraftPoolDie(int pos);


    public abstract Die getDiceBagDie();

    public abstract void putDiceBagDie(Die die);


    public abstract boolean setskipSecondTurn(Player player);

    public abstract boolean getSkipSecondTurn(Player player);

    public abstract int calculatePrivatePoints(Player player);

    public abstract int calculatePublicPoints(Player player);

    public abstract int calculateUnusedCellPoints(Player player);

    public abstract PrivateObjective getPrivateObjective(Player player);

    public abstract PublicObjective[] getpublicObjectives();


    public abstract boolean endRound();

    public abstract boolean startRound();

    public abstract boolean nextTurn();

}
