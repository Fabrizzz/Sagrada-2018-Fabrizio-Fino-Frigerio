package it.polimi.se2018.server;

import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMIInterface extends Remote {
    boolean addClient(Connection connection)  throws RemoteException;
    void reciveMessage(Message message, Connection connection)  throws RemoteException;
}
