package it.polimi.se2018.model;

import java.io.Serializable;

public class Player implements Serializable { //da completare
    private String nick;
    private int favorTokens;
    private boolean isConnected;

    private boolean skipSecondTurn; //per la tool card numero 8
    private boolean canDoTwoTurn;
    private boolean isYourTurn = false;

    public Player(String nick){
        this.nick = nick;
    }
    public boolean isYourTurn() {
        return isYourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        isYourTurn = yourTurn;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
