package it.polimi.se2018.rmi.server;

import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Servizio server RMI
 * @author Alessio
 */
public class ServerRMIImplementation extends UnicastRemoteObject {
    private ServerNetwork serverNetwork;

    /**
     * Costruttore
     * @param serverNetwork Gestore rete server
     * @throws RemoteException
     */
    public ServerRMIImplementation(ServerNetwork serverNetwork) throws RemoteException {
        super(0);
        this.serverNetwork = serverNetwork;
    }

    /**
     * Aggiunge client alla lista di connessioni del gestore di rete del server
     * @param connection connessione da aggiungere
     */
    public boolean addClient(Connection connection){
        return serverNetwork.addClient(connection);
    }

    /**
     * Metodo chiamato dal client quando deve inviare un messaggio al server
      * @param message messaggio inviato dal client
     * @param connection connessione
     */
    public void reciveMessage(Message message,Connection connection){
        serverNetwork.reciveMessage(message,connection);
    }
}
