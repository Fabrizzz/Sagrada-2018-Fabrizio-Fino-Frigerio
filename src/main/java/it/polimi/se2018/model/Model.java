package it.polimi.se2018.model;

import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.utils.Tool;
import it.polimi.se2018.utils.exceptions.SizeLimitExceededException;

import java.util.*;

public class Model extends Observable {

    //Define
    private static final int numberOfToolCards = 3;
    private static final int numberOfPublicObjectives = 3;
    private static final int numberOfDicePerColor = 18;     //Nei 90 dadi ho 18 dadi per colore; passo 18 al costruttore del diceBag

    private final Map<Player, PlayerBoard> boardMap;
    private final Map<Player, PrivateObjective> privateObjectiveMap;
    private final List<Tool> tools;
    private final List<PublicObjective> publicObjective = new ArrayList<>();

    private final DiceBag diceBag;    //Il sacchetto contenente i dadi
    private final DraftPool draftPool;  //Dadi pescati del round
    private final RoundTrack roundTrack; //Il tracciato
    private final List<Player> players;   //I player in gioco

    private int round = 0;
    private int turn = 0;
    private boolean firstTurn = true; //ogni round è fatto da due turni, è importante tenerne conto anche per l' uso di certe tool
    private boolean usedTool = false;
    private boolean normalMove = false;


    //Costruttore
    public Model(Player[] players, List<PublicObjective> publicObjectives, Map<Player, PlayerBoard> boardMap, Map<Player, PrivateObjective> privateObjectiveMap) {
        if (players.length >= 4)
            throw new IllegalArgumentException("Too many players");
        this.players = new ArrayList<>(Arrays.asList(players)); //Creazione di un arraylist che conterrà i player
        diceBag = new DiceBag(numberOfDicePerColor);    //Creazione del sacchetto; viene passato 18 così verranno generati i 90 dadi, 18 per colore
        draftPool = new DraftPool(players.length, diceBag); //Creazione della variabile draftPool per i dadi pescati; viene passato il numero di dadi da pescare (numplayer  2 +1) e il sacchetto da cui pescare
        tools = Tool.getRandTools(numberOfToolCards);
        if (publicObjectives.size() != numberOfPublicObjectives)
            throw new IllegalArgumentException("Wrong number of public Objectives");
        publicObjective.addAll(publicObjectives);
        roundTrack = new RoundTrack();
        if (boardMap.size() != players.length)
            throw new IllegalArgumentException("Wrong number of boardMaps");

        this.boardMap = boardMap;
        if (privateObjectiveMap.size() != players.length)
            throw new IllegalArgumentException("Wrong number of Private Objectives");

        this.privateObjectiveMap = privateObjectiveMap;
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

    public List<Tool> getTools() {
        return tools;
    }

    /*public abstract int calculatePrivatePoints(Player player);

    public abstract int calculatePublicPoints(Player player);

    public abstract int calculateUnusedCellPoints(Player player);*/

    public PrivateObjective getPrivateObjective(Player player) {

        return privateObjectiveMap.get(player);
    }

    public List<PublicObjective> getPublicObjectives() {
        return publicObjective;
    }

    public void setPublicObjective(PublicObjective publicObjective) throws SizeLimitExceededException {
        if (this.publicObjective.size() >= numberOfPublicObjectives)
            throw new SizeLimitExceededException();
        this.publicObjective.add(publicObjective);
    }


    public void endRound() {
        turn = 0;
        firstTurn = true;
        roundTrack.addDice(round, draftPool.removeAll());
        round++;
        if (round == 10)
            endGame();
        else {
            draftPool.rollDice(diceBag);
        }

    }

    public void endGame() {

    }

    public void nextTurn() {
        usedTool = false;
        normalMove = false;
        int playerPosition = (turn < players.size()) ? turn : players.size() * 2 - turn - 1;
        players.get(playerPosition).setYourTurn(false);
        turn++;

        if (turn == players.size() * 2)
            endRound();
        else if (turn == players.size())
            firstTurn = false;
        if (round != 10) {
            
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
