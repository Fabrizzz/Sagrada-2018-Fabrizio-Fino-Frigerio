package it.polimi.se2018.rmi.server;

import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.Connection;
import it.polimi.se2018.utils.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMIImplementation extends UnicastRemoteObject {
    private ServerNetwork serverNetwork;

    public ServerRMIImplementation(ServerNetwork serverNetwork) throws RemoteException {
        super(0);
        this.serverNetwork = serverNetwork;
    }

    public void addClient(Connection connection){
        serverNetwork.addClient(connection);
    }

    public void reciveMessage(Message message,Connection connection){
        serverNetwork.reciveMessage(message,connection);
    }
}
