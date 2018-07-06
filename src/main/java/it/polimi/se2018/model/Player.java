package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * Player
 * @author Giampietro
 */
public class Player implements Serializable {
    private String nick;
    private long id;
    private int favorTokens;
    private boolean changed = false;

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

    /**
     * Return if the player must skip the second turn this round
     * @return
     */
    public boolean isSkipSecondTurn() {
        return skipSecondTurn;
    }

    /**
     * Set if the player must skip the second turn this round
     * @param skipSecondTurn
     */
    public void setSkipSecondTurn(boolean skipSecondTurn) {
        this.skipSecondTurn = skipSecondTurn;
        setChanged();
    }

    /**
     * Return the number of tokens of the player
     * @return the number of tokens
     */
    public int getFavorTokens() {
        return favorTokens;
    }

    /**
     * Set the number of tokens of the player
     * @param favorTokens the number of tokens
     */
    public void setFavorTokens(int favorTokens) {
        this.favorTokens = favorTokens;
        setChanged();
    }

    /**
     * Return if the player can do a second turn
     * @return true if can do a second turn, false otherwise
     */
    public boolean isCanDoTwoTurn() {
        return canDoTwoTurn;

    }

    /**
     * Set if the player can do two turns
     * @param canDoTwoTurn
     */
    public void setCanDoTwoTurn(boolean canDoTwoTurn) {
        this.canDoTwoTurn = canDoTwoTurn;
        setChanged();
    }

    /**
     * Get the player nickname
     * @return
     */
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
        setChanged();
    }


    public Long getId(){ //da rimuovere sia dal player che dalla modelview
        return id;
    }

    /**
     * It set Changed to True
     */
    public synchronized void setChanged() {
        changed = true;
    }

    /**
     * It tells if something has changed from the last ModelViewUpdate
     *
     * @return
     */
    public synchronized boolean isChanged() {
        boolean temp = changed;
        changed = false;
        return temp;
    }
}
