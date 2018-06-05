package it.polimi.se2018.server.rmi;

import it.polimi.se2018.utils.network.RMIInterfaceRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Server rmi interface
 * @author Alessio
 */
public interface ServerRMIInterface extends Remote {
    RMIInterfaceRemote addClient(RMIInterfaceRemote connection)  throws RemoteException;
}
