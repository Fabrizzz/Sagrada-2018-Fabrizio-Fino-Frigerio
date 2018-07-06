package it.polimi.se2018.view;

import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.Observable;
import java.util.Observer;

/**
 * view abstract class
 */
public abstract class View extends Observable implements Observer {
    /**
     * Notifica il giocatore che la connessione e' stata chiusa
     */
    public abstract void connectionClosed();

    public abstract void elaborateMessage(ServerMessage message);

}
