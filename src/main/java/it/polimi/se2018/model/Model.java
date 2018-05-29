package it.polimi.se2018.model;

import it.polimi.se2018.controller.RoundTimer;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.SizeLimitExceededException;

import java.util.*;

/**
 * Game model
 * @author Giampietro
 */
public class Model extends Observable {

    private static final int NUMBER_OF_TOOL_CARDS = 3;
    private static final int NUMBER_OF_PUBLIC_OBJECTIVES = 3;
    private static final int NUMBER_OF_DICE_PER_COLOR = 18;     //Nei 90 dadi ho 18 dadi per colore; passo 18 al costruttore del diceBag
    private static final int MINUTES_PER_TURN = 1;

    private final Map<Player, PlayerBoard> boardMap;
    private final Map<Player, PrivateObjective> privateObjectiveMap;
    private final Map<Tool, Boolean> tools;
    private final List<PublicObjective> publicObjective = new ArrayList<>(NUMBER_OF_PUBLIC_OBJECTIVES);


    private final DiceBag diceBag;    //Il sacchetto contenente i dadi
    private final DraftPool draftPool;  //Dadi pescati del round
    private final RoundTrack roundTrack; //Il tracciato
    private final List<Player> players;   //I player in gioco

    private int round = 0;
    private int turn = 0;
    private boolean firstTurn = true; //ogni round è fatto da due turni, è importante tenerne conto anche per l' uso di certe tool
    private boolean usedTool = false;
    private boolean normalMove = false;
    private boolean timerScaduto = false;

    private Timer timer = new Timer();


    /**
     * Constructor
     * @param players array of the players
     * @param publicObjectives list of the public objective
     * @param boardMap map between the players and the boards
     * @param privateObjectiveMap map between the players and their private objectives
     */
    public Model(Player[] players, List<PublicObjective> publicObjectives, Map<Player, PlayerBoard> boardMap, Map<Player, PrivateObjective> privateObjectiveMap) {
        if (players.length >= 4)
            throw new IllegalArgumentException("Too many players");
        this.players = new ArrayList<>(Arrays.asList(players)); //Creazione di un arraylist che conterrà i player
        diceBag = new DiceBag(NUMBER_OF_DICE_PER_COLOR);    //Creazione del sacchetto; viene passato 18 così verranno generati i 90 dadi, 18 per colore
        draftPool = new DraftPool(players.length, diceBag); //Creazione della variabile draftPool per i dadi pescati; viene passato il numero di dadi da pescare (numplayer  2 +1) e il sacchetto da cui pescare
        tools = new EnumMap<>(Tool.class);
        Tool.getRandTools(NUMBER_OF_TOOL_CARDS).forEach(k -> tools.put(k, false));
        if (publicObjectives.size() != NUMBER_OF_PUBLIC_OBJECTIVES)
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

    /**
     * Return the current round number
     * @return the number of the current round
     */
    public int getRound() {
        return round;
    }

    /**
     * Set the round number
     * @param round new round number
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * Return if is the first turn in the round
     * @return true if is the first turn, false otherwise
     */
    public boolean isFirstTurn() {
        return firstTurn;
    }

    /**
     * Set the value of firtTurn
     * @param firstTurn new value of firstTurn
     */
    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }

    /**
     * Return if a player has used a tool card in this round
     * @return true if a player has userd a tool card in this round
     */
    public boolean hasUsedTool() {
        return usedTool;
    }

    /**
     * Set the value of usedTool
     * @param usedTool new value of usedTool
     */
    public void setUsedTool(boolean usedTool) {
        this.usedTool = usedTool;
    }

    /**
     * Return if a player has done a normal move this round
     * @return true if a player has done a normal move this round, false otherwise
     */
    public boolean hasUsedNormalMove() {
        return normalMove;
    }

    /**
     * Set the value of normalMove
     * @param normalMove new value of normalMove
     */
    public void setNormalMove(boolean normalMove) {
        this.normalMove = normalMove;
    }

    /**
     * Return the boardMap between players and boards
     * @return map between plauyers and boards
     */
    protected Map<Player, PlayerBoard> getBoardMap() {
        return boardMap;
    }

    /**
     * Return the board of a player
     * @param player player
     * @return board
     */
    public PlayerBoard getBoard(Player player) {
        return boardMap.get(player);
    }

    /**
     * Return the diceBag
     * @return diceBag
     */
    public DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     * Return the draftpool
     * @return draftpool
     */
    public DraftPool getDraftPool() {
        return draftPool;
    }

    /**
     * Return the round tack
     * @return roundTrack
     */
    public RoundTrack getRoundTrack() {
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
     * Return the list of the toolcards
     * @return the list of the toolcards
     */
    public Map<Tool, Boolean> getTools() {
        return tools;
    }

    /*public abstract int calculatePrivatePoints(Player player);

    public abstract int calculatePublicPoints(Player player);

    public abstract int calculateUnusedCellPoints(Player player);*/

    /**
     * Return the private objective of a player
     * @param player player
     * @return  private objective of player
     * @throws IllegalArgumentException if plauyer is not contained in the player list
     */
    public PrivateObjective getPrivateObjective(Player player) throws IllegalArgumentException{
        if(players.contains(player)) {
            return privateObjectiveMap.get(player);
        }else{
            throw new IllegalArgumentException("Player non presente");
        }
    }

    /**
     * Return the list of the public objectives
     * @return list of the public objectives
     */
    public List<PublicObjective> getPublicObjectives() {
        return publicObjective;
    }

    /**
     * Add a objective to the public objective list
     * @param publicObjective objective to be added
     * @throws SizeLimitExceededException if the list is full
     */
    public void setPublicObjective(PublicObjective publicObjective) throws SizeLimitExceededException {
        if (this.publicObjective.size() >= NUMBER_OF_PUBLIC_OBJECTIVES)
            throw new SizeLimitExceededException();
        this.publicObjective.add(publicObjective);
    }

    /**
     * End the round and update the round variables
     */
    private void endRound() {
        turn = 0;
        firstTurn = true;
        roundTrack.addDice(round, draftPool.removeAll());
        round++;
        Collections.rotate(players, 1);
        if (round == 10)
            endGame();
        else {
            draftPool.rollDice(diceBag);
        }

    }

    /**
     * End the game
     */
    public void endGame() {
        timer.purge();
        timer.cancel();
    }

    /**
     * Change the current player turn
     */
    public void nextTurn() { //da spostare nel controller
        //aggiungere che se rimane un solo giocatore, ha vinto
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
            if (players.get(playerPosition).isSkipSecondTurn()) {
                players.get(playerPosition).setSkipSecondTurn(false);
                nextTurn();
            } else {
                players.get(playerPosition).setYourTurn(true);
                if (!players.get(playerPosition).isConnected())
                    nextTurn();
                else {
                    timer.schedule(new RoundTimer(getTurn(), getRound(), this), MINUTES_PER_TURN * 60 * 1000);
                    notifyObs();
                }
            }
        } else
            endGame();
    }


    public synchronized boolean isTimerScaduto() {
        return timerScaduto;
    }

    public synchronized void setTimerScadutoOff() {
        this.timerScaduto = false;
    }

    public synchronized void setTimerScadutoOn(int turn, int round) {
        if (getTurn() == turn && this.getRound() == round)
            timerScaduto = true;
    }

    public int getTurn() {
        return turn;
    }

    public void notifyObs() {
        setChanged();
        notifyObservers();
    }

}
