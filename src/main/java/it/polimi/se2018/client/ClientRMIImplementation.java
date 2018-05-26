package it.polimi.se2018.client;

import it.polimi.se2018.utils.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote class of the client
 * @author Alessio
 */
public class ClientRMIImplementation implements Remote{

    /**
     * Called when the server wants to send a message to the client
     * @param message message to send
     * @throws RemoteException rmi error
     */
    public void sendMessage(Message message) throws RemoteException {
        //gestione messaggio lato client
    }

    /**
     * Signal the client to close the connection
     * @throws RemoteException rmi error
     */
    public void close() throws RemoteException{
        //il client chiude la connessione
    }
}
