package it.polimi.se2018.rmi.client;

import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.Message;

import java.rmi.Remote;

/**
 * Classe remote del client
 * @author Alessio
 */
public class ClientRMIImplementation implements Remote,Connection{

    /**
     * Metodo richiamato in remoto dal server quando deve inviare un messaggio al client
     * @param message messaggio da inviare
     * @return true se viene inviato, false altrimenti
     */
    public boolean sendMessage(Message message) {
        //gestione messaggio lato client
        return false;
    }

    public void close(){}//todo
}
