package it.polimi.se2018.utils;

import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.utils.Message;

/**
 * Interfaccia di connessione tra client e server, implementata da ClientSocketConnection e ClientRmiConnection
 * @author Alessio
 */
public interface Connection {
    /**
     * Invia messaggio al client
     * @param message messaggio da inviare
     */
    public void sendMessage(Message message);
}
