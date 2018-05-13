package it.polimi.se2018.model;

import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.utils.Tool;

import java.util.*;

public abstract class Model extends Observable {

    //Define
    private static final int numberOfToolCards = 3;
    private static final int numberOfPublicObjectives = 3;
    private static final int numberOfDicePerColor = 18;     //Nei 90 dadi ho 18 dadi per colore; passo 18 al costruttore del diceBag

    private Map<Player, PlayerBoard> boardMap;
    private Map<Player, PrivateObjective> privateObjectiveMap;
    private final Tool[] tools;
    private final PublicObjective[] publicObjective;

    private int round = 0;
    private int turn = 0;
    private boolean firstTurn = true; //ogni round è fatto da due turni, è importante tenerne conto anche per l' uso di certe tool
    private boolean usedTool;
    private boolean normalMove;

    private DiceBag diceBag;    //Il sacchetto contenente i dadi
    private DraftPool draftPool;  //Dadi pescati del round
    private RoundTrack roundTrack; //Il tracciato
    private List<Player> players;   //I player in gioco




    //Costruttore
    public Model(Player[] players) {
        this.players = new ArrayList<>(Arrays.asList(players)); //Creazione di un arraylist che conterrà i player
        diceBag = new DiceBag(numberOfDicePerColor);    //Creazione del sacchetto; viene passato 18 così verranno generati i 90 dadi, 18 per colore
        draftPool = new DraftPool(players.length, diceBag); //Creazione della variabile draftPool per i dadi pescati; viene passato il numero di dadi da pescare (numplayer  2 +1) e il sacchetto da cui pescare
        tools = Tool.getRandTools(numberOfToolCards);
        publicObjective = new PublicObjective[3];
        this.players = new ArrayList<>(Arrays.asList(players));
        players[0].setYourTurn(true);

    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isFirstTurn() {
        return firstTurn;
    }

    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }

    public boolean HasUsedTool() {
        return usedTool;
    }

    public void setUsedTool(boolean usedTool) {
        this.usedTool = usedTool;
    }

    public boolean HasUsedNormalMove() {
        return normalMove;
    }

    public void setNormalMove(boolean normalMove) {
        this.normalMove = normalMove;
    }

    protected Map<Player, PlayerBoard> getBoardMap() {
        return boardMap;
    }

    public PlayerBoard getBoard(Player player) {
        return boardMap.get(player);
    }

    public DiceBag getDiceBag() {
        return diceBag;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Tool[] getTools() {
        return tools;
    }

    /*public abstract int calculatePrivatePoints(Player player);

    public abstract int calculatePublicPoints(Player player);

    public abstract int calculateUnusedCellPoints(Player player);*/

    public PrivateObjective getPrivateObjective(Player player) {

        return privateObjectiveMap.get(player);
    }

    public PublicObjective[] getPublicObjectives() {
        return publicObjective;
    }


    public void endRound() {
        turn = 0;
        firstTurn = true;
        round++;
    }

    public void endGame() {

    }

    public void nextTurn() {
        int playerPosition = (turn < players.size()) ? turn : players.size() * 2 - turn - 1;
        players.get(playerPosition).setYourTurn(false);
        turn++;

        if (turn == players.size() * 2)
            endRound();
        else if (turn == players.size())
            firstTurn = false;
        if (round == 10) {
            endGame();
        } else {

            playerPosition = (turn < players.size()) ? turn : players.size() * 2 - turn - 1;
            players.get(playerPosition).setYourTurn(true);


            if (!players.get(playerPosition).isYourTurn())
                nextTurn();
        }
    }


    public void notifyObs() {
        setChanged();
        notifyObservers();
    }

}
