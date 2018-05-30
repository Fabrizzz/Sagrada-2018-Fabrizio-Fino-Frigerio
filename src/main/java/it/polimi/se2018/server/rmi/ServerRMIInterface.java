package it.polimi.se2018.server.rmi;

import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Rmi interface
 * @author Alessio
 */
public interface ServerRMIInterface extends Remote {
    ServerRmiConnection addClient(Connection connection)  throws RemoteException;
}
