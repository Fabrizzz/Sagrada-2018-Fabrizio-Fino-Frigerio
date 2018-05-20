package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.Message;

/**
 * Interfaccia gestore di rete
 * @author Alessio
 */
public interface NetworkHandler {

    /**
     * Rimuove connessione
     * @param connection connessione da rimuovere
     */
    public void removeConnection(Connection connection);

    /**
     * Metodo chiamato quando viene ricevuto un messaggio
     * @param message messaggio ricevuto
     * @param connection connessione
     */
    public void reciveMessage(Message message,Connection connection);

}
