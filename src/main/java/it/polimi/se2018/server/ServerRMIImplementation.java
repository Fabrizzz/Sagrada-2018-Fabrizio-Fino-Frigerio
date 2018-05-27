package it.polimi.se2018.server;

import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server rmi service
 * @author Alessio
 */
public class ServerRMIImplementation extends UnicastRemoteObject implements ServerRMIInterface {
    private ServerNetwork serverNetwork;

    /**
     * Costructor
     * @param serverNetwork Server network manager
     * @throws RemoteException rmi error
     */
    public ServerRMIImplementation(ServerNetwork serverNetwork) throws RemoteException {
        super(0);
        this.serverNetwork = serverNetwork;
    }

    /**
     * Add a client connection to the connection map on the server
     * @param connection connection to add
     * @return if the connection was correctly added
     */
    public boolean addClient(Connection connection) throws RemoteException{
        return serverNetwork.addClient(connection);
    }

    /**
      * Method called by the client to send a message to the server
      * @param message message to send
     * @param connection connection
     */
    public void reciveMessage(Message message,Connection connection) throws RemoteException{
        serverNetwork.reciveMessage(message,connection);
    }

}
