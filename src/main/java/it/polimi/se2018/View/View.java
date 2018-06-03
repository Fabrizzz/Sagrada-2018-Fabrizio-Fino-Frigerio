package it.polimi.se2018.View;

import java.util.Observable;
import java.util.Observer;

public abstract class View extends Observable implements Observer {
    /**
     * Notifica il giocatore che la connessione e' stata chiusa
     */
    public abstract void connectionClosed();
}
