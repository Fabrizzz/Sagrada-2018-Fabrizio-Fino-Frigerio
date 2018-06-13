package it.polimi.se2018.server.rmi;

import it.polimi.se2018.utils.network.RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Server rmi interface
 * @author Alessio
 */
public interface ServerRMIControllerInterface extends Remote {
    RMIInterface addClient(RMIInterface connection) throws RemoteException;
}
