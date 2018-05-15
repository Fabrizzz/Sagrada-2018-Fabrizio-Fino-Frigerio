package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * Giocatore
 * @author Giampietro
 */
public class Player implements Serializable { //da completare
    private String nick;
    private int favorTokens;
    private boolean isConnected;

    private boolean skipSecondTurn; //per la tool card numero 8
    private boolean canDoTwoTurn;
    private boolean isYourTurn = false;

    /**
     * Costruttore
     * @param nick nickname del giocatore
     */
    public Player(String nick){
        this.nick = nick;
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
