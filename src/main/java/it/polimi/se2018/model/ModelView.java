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

    /**
     * Costruttore
     * @param model modello del gioco
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
        normalMove = model.HasUsedNormalMove();


        model.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        Model model = (Model) o;
        round = model.getRound();
        firstTurn = model.isFirstTurn();
        usedTool = model.hasUsedTool();
        normalMove = model.HasUsedNormalMove();

    }

    /**
     * Ritorna il numero del round corrente
     * @return numero del round corrente
     */
    public int getRound() {
        return round;
    }

    /**
     * Restituisce se e' il primo turno del giro
     * @return true se e' il primo turno del giro,false altrimenti
     */
    public boolean isFirstTurn() {
        return firstTurn;
    }

    /**
     * Restituisce se e' stata usata una carta oggetto nel giro corrente
     * @return true se e' stata usata una carta tool,false altrimenti
     */
    public boolean isUsedTool() {
        return usedTool;
    }

    /**
     * Restituisce se e' stata fatta una mossa normale nel giro corrente
     * @return true se e' stata effettuata una mossa normale, false altrimenti
     */
    public boolean isNormalMove() {
        return normalMove;
    }

    /**
     * Restituisce la plancia di un giocatore
     * @param player giocatore
     * @return la plancia di player
     */
    private PlayerBoard getBoard(Player player) {
        return boardMap.get(player);
    }

    /**
     * Restituisce il sacchetto dei dadi
     * @return il sacchetto dei dadi
     */
    private DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     * Restituisce la riserva dei dadi
     * @return la riserva dei dadi
     */
    private DraftPool getDraftPool() {
        return draftPool;
    }

    /**
     * Resistuisce il tracciato dei dadi
     * @return il tracciato dei dadi
     */
    private RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * Restituisce la lista dei giocatori
     * @return la lista dei giocatori
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Restituisce la lista delle carte oggetto
     * @return la lista delle carte oggetto
     */
    public List<Tool> getTools() {
        return tools;
    }

    /**
     * Restituisce se la cella nella posizione specificata nella plancia di un giocatore contiene un dado
     * @param player giocatore
     * @param row riga della cella
     * @param column colonna della cella
     * @return true se contiene un dado, false altimenti
     */
    public boolean boardContainsDie(Player player, int row, int column) {
        return getBoard(player).containsDie(row, column);
    }

    /**
     * Restituisce il dado contenuto nella plancia di un giocatore nella posizione specificata
     * @param player giocatore
     * @param row riga della cella
     * @param column colonna della cella
     * @return il dado nella cella in posizione row,col
     * @throws NoDieException se non e' presente un dado nella cella
     */
    public Die getBoardDie(Player player, int row, int column) throws NoDieException {

        return getBoard(player).getDie(row, column);
    }

    /**
     * Restituisce il dado nel tracciato dei dadi in posizione pos nel giro round
     * @param round giro
     * @param pos posizione del dado nel giro
     * @return il dado in posizione round,pos nel tracciato
     */
    public Die getRoundTrackDie(int round, int pos) {
        return getRoundTrack().getDie(round, pos);
    }

    /**
     * Restituisce se il tracciato dei dadi contiene almeno un dado di colore color
     * @param color colore da cercare
     * @return true se contiene un dado di colore color,false altrimenti
     */
    public boolean roundTrackContainsColor(Color color) {
        return getRoundTrack().hasColor(color);
    }

    /**
     * Restituisce il numero di dadi nella riserva
     * @return numero di dadi nella riserva
     */
    public int DraftPoolSize() {
        return getDraftPool().size();
    }

    /**
     * Restituisce il dado nella riserva in posizione pos
     * @param pos posizione del dado nella riserva
     * @return il dado in posizione pos nella riserva
     * @throws NoDieException se non e' presente nessun dado in posizione pos
     */
    public Die getDraftPoolDie(int pos) throws NoDieException {
        return getDraftPool().getDie(pos);
    }

    /**
     * Estrae un dado dal sacchetto
     * @return dado estratto dal sacchetto
     */
    public Die getDiceBagDie() {
        return getDiceBag().takeDie();
    }

    /**
     * Restituisce la lista degli obiettivi publici
     * @return lista degli obiettivi pubblici
     */
    public List<PublicObjective> getPublicObjective() {
        return publicObjective;
    }

}
