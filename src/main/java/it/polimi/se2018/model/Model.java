package it.polimi.se2018.model;

import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.SizeLimitExceededException;

import java.util.*;

/**
 * Modello
 * @author Giampietro
 */
public class Model extends Observable {

    //Define
    private static final int numberOfToolCards = 3;
    private static final int numberOfPublicObjectives = 3;
    private static final int numberOfDicePerColor = 18;     //Nei 90 dadi ho 18 dadi per colore; passo 18 al costruttore del diceBag

    private final Map<Player, PlayerBoard> boardMap;
    private final Map<Player, PrivateObjective> privateObjectiveMap;
    private final List<Tool> tools;
    private final List<PublicObjective> publicObjective = new ArrayList<>(numberOfPublicObjectives);

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


    /**
     * Costruttore
     * @param players Array dei giocatori
     * @param publicObjectives Lista degli obiettivi pubblici
     * @param boardMap Mappa tra i player e le loro plancie
     * @param privateObjectiveMap mappa tra i player e i loro obiettivi privati
     */
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

    /**
     * Restituisce il giro corrente
     * @return il giro corrente
     */
    public int getRound() {
        return round;
    }

    /**
     * Imposta il giro di gioco
     * @param round nuovo valore del giro
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * Ritorna se e' il primo turno del giro
     * @return true se e' il primo turno del giro, false altrimenti
     */
    public boolean isFirstTurn() {
        return firstTurn;
    }

    /**
     * Imposta il valore di firstTurn
     * @param firstTurn nuovo valore di firstTurn
     */
    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }

    /**
     * Ritorna se e' stato usato una carta tool
     * @return true se e' stata usata nel giro, false altrimenti
     */
    public boolean hasUsedTool() {
        return usedTool;
    }

    /**
     * Imposta il valore di usedTool
     * @param usedTool nuovo valore di usedTool
     */
    public void setUsedTool(boolean usedTool) {
        this.usedTool = usedTool;
    }

    /**
     * Ritorna se e' stata fatta una mossa normale
     * @return true se e' stata effettuata una mossa normale, false altrimenti
     */
    public boolean HasUsedNormalMove() {
        return normalMove;
    }

    /**
     * Imposta il valore di normalMove
     * @param normalMove nuovo valore di normalMove
     */
    public void setNormalMove(boolean normalMove) {
        this.normalMove = normalMove;
    }

    /**
     * Restituisce la mappa boardMap
     * @return mappa tra i giocatori e le plancie
     */
    protected Map<Player, PlayerBoard> getBoardMap() {
        return boardMap;
    }

    /**
     * Restituisce la plancia di un giocatore
     * @param player giocatore
     * @return plancia del giocatore
     */
    public PlayerBoard getBoard(Player player) {
        return boardMap.get(player);
    }

    /**
     * Restituisce il sacchetto dei dadi
     * @return il sacchetto dei dadi
     */
    public DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     * Restituisce la riserva dei dadi
     * @return riserva dei dadi
     */
    public DraftPool getDraftPool() {
        return draftPool;
    }

    /**
     * Restituisce il tracciato dei dadi
     * @return tracciato dei dadi
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * Restituisce la lista dei giocatori
     * @return  lista dei giocatori
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Restituisce la lista delle carte tool
     * @return la lista delle carte tool
     */
    public List<Tool> getTools() {
        return tools;
    }

    /*public abstract int calculatePrivatePoints(Player player);

    public abstract int calculatePublicPoints(Player player);

    public abstract int calculateUnusedCellPoints(Player player);*/

    /**
     * Restituisce l'obiettivo privato di un giocatore
     * @param player giocatore
     * @return  obiettivo privato del giocatore
     * @throws IllegalArgumentException se il giocatore non e' prensente nella lista dei giocatori
     */
    public PrivateObjective getPrivateObjective(Player player) throws IllegalArgumentException{
        if(players.contains(player)) {
            return privateObjectiveMap.get(player);
        }else{
            throw new IllegalArgumentException("Player non presente");
        }
    }

    /**
     * Restituisce la lista degli obiettivi pubblici
     * @return la lista degli obiettivi pubblici
     */
    public List<PublicObjective> getPublicObjectives() {
        return publicObjective;
    }

    /**
     * Aggiunge un obiettivo pubblico alla lista
     * @param publicObjective obiettivo da aggiungere
     * @throws SizeLimitExceededException se e'stato raggiunto in numero massimo di obiettivi pubblici
     */
    public void setPublicObjective(PublicObjective publicObjective) throws SizeLimitExceededException {
        if (this.publicObjective.size() >= numberOfPublicObjectives)
            throw new SizeLimitExceededException();
        this.publicObjective.add(publicObjective);
    }

    /**
     * Termina il round reimpostando le variabili di round
     */
    public void endRound() {
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
     * Termina la partita
     */
    public void endGame() {}

    /**
     * Passa al turno del giocatore successivo
     */
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

    public synchronized boolean isTimerScaduto() {
        return timerScaduto;
    }

    public synchronized void setTimerScaduto(boolean timerScaduto) {
        this.timerScaduto = timerScaduto;
    }

    public void notifyObs() {
        setChanged();
        notifyObservers();
    }

}
