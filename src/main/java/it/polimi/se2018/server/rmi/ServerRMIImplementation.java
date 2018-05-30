package it.polimi.se2018.server.rmi;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.Message;

import java.rmi.Remote;
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


    public ServerRmiConnection addClient(Connection connection) throws RemoteException{
        ServerRmiConnection serverRmiConnection = new ServerRmiConnection(serverNetwork);
        RemoteView remoteView = serverNetwork.addClient(connection);
        remoteView.addObserver(connection);
        if(remoteView != null){
            return ((ServerRmiConnection) UnicastRemoteObject.exportObject(serverRmiConnection, 0));
        }else{
            return null;
        }

    }

}
