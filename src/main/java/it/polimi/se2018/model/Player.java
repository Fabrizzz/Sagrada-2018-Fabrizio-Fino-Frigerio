package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * Player
 * @author Giampietro
 */
public class Player implements Serializable { //da completare
    private String nick;
    private long id;
    private int favorTokens;

    private boolean skipSecondTurn; //per la tool card numero 8
    private boolean canDoTwoTurn;
    private boolean isYourTurn = false;


    /**
     * Constructor
     * @param nick nickname of the player
     * @param id id of the player
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

    public String getNick(){return nick;}

    /**
     * Return if is the player turn
     * @return true if is the player turn, false otherwise
     */
    public boolean isYourTurn() {
        return isYourTurn;
    }

    /**
     * Set the value of yourTurb
     * @param yourTurn new value of yourTurn
     */
    public void setYourTurn(boolean yourTurn) {
        isYourTurn = yourTurn;
    }


    public Long getId(){return id;}
}
