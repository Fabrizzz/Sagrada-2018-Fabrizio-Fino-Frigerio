package it.polimi.se2018.server.rmi;

import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.network.RMIConnection;
import it.polimi.se2018.utils.network.RMIInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server rmi service
 * @author Alessio
 */
public class ServerRMIController extends UnicastRemoteObject implements ServerRMIControllerInterface {
    private ServerNetwork serverNetwork;

    /**
     * Costructor
     * @param serverNetwork Server network manager
     * @throws RemoteException rmi error
     */
    public ServerRMIController(ServerNetwork serverNetwork) throws RemoteException {
        super(0);
        this.serverNetwork = serverNetwork;
    }

    /**
     * Add a client to the server connection list
     * @param rmiInterface client remote object reference
     * @return   if the server has accepted the client
     * @throws RemoteException if a rmi error occours
     */
    public RMIInterface addClient(RMIInterface rmiInterface) throws RemoteException {

        RMIConnection RMIConnection = new RMIConnection(rmiInterface);

        RMIConnection.addObserver(serverNetwork);

        return ((RMIInterface) UnicastRemoteObject.exportObject(RMIConnection, 0));
    }

}
