package it.polimi.se2018.client;

import it.polimi.se2018.utils.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Classe remote del client
 * @author Alessio
 */
public class ClientRMIImplementation implements Remote{

    /**
     * Metodo richiamato in remoto dal server quando deve inviare un messaggio al client
     * @param message messaggio da inviare
     */
    public void sendMessage(Message message) throws RemoteException {
        //gestione messaggio lato client
    }

    public void close() throws RemoteException{
        //il client chiude la connessione
    }
}
