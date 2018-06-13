package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by the remote object in the clinet and server
 * @author Alessio
 */
public interface RMIInterface extends Remote {
    void remoteSend(Message message) throws RemoteException;
}

