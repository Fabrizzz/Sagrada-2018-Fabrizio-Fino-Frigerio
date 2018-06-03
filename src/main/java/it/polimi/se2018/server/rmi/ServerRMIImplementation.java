package it.polimi.se2018.server.rmi;

import it.polimi.se2018.client.ClientRMIConnection;
import it.polimi.se2018.client.RMIInterfaceRemote;
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


    public RMIInterfaceRemote addClient(RMIInterfaceRemote rmiInterfaceRemote) throws RemoteException{

        ClientRMIConnection clientRMIConnection = new ClientRMIConnection(rmiInterfaceRemote);
        ServerRMIImplementationRemote serverRmiConnection = new ServerRMIImplementationRemote(serverNetwork,clientRMIConnection);

        if(serverNetwork.addClient(clientRMIConnection)){
            return ((RMIInterfaceRemote) UnicastRemoteObject.exportObject(serverRmiConnection, 0));
        }else{
            return null;
        }

    }

}
