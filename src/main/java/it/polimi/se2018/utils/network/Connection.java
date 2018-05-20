package it.polimi.se2018.utils.network;

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
    public boolean sendMessage(Message message);
}
