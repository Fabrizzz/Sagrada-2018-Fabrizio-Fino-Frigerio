package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * Giocatore
 * @author Giampietro
 */
public class Player implements Serializable { //da completare
    private String nick;
    private long id;
    private int favorTokens;
    private boolean isConnected;

    private boolean skipSecondTurn; //per la tool card numero 8
    private boolean canDoTwoTurn;
    private boolean isYourTurn = false;


    /**
     * Costruttore

     * @param nick
     * @param id
     */
    public Player(String nick, Long id) {
        this.nick = nick;
        this.id = id;
    }

    public boolean isSkipSecondTurn() {
        return skipSecondTurn;
    }

    public void setSkipSecondTurn(boolean skipSecondTurn) {
        this.skipSecondTurn = skipSecondTurn;
    }

    public int getFavorTokens() {
        return favorTokens;
    }

    public void setFavorTokens(int favorTokens) {
        this.favorTokens = favorTokens;
    }

    public boolean isCanDoTwoTurn() {
        return canDoTwoTurn;

    }

    public void setCanDoTwoTurn(boolean canDoTwoTurn) {
        this.canDoTwoTurn = canDoTwoTurn;
    }



    /**
     * Restituisce se e' il turno del giocatore
     * @return true se e' il turno del giocatore, false altrimenti
     */
    public boolean isYourTurn() {
        return isYourTurn;
    }

    /**
     * Imposta il valore di yourTurn
     * @param yourTurn il nuovo valore di yourTurn
     */
    public void setYourTurn(boolean yourTurn) {
        isYourTurn = yourTurn;
    }

    /**
     * Restituisce se il giocatore e' connesso
     * @return true se il giocatore e' connesso, false altrimenti
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Imposta se il giocatore e' connessio
     * @param connected nuovo valore di isConnected
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
