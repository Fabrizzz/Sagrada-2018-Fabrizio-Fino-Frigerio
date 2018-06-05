package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

/**
 * Interface used by the remote object in the clinet and server
 * @author Alessio
 */
public interface RMIInterfaceRemote extends Remote{
    public boolean sendMessage(Message message) throws RemoteException;

    public boolean isConnected() throws RemoteException;

    public void close() throws RemoteException;

    public void update(Observable o, Object arg) throws RemoteException;
}

