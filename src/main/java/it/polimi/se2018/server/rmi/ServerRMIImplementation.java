package it.polimi.se2018.server.rmi;

import it.polimi.se2018.client.ClientRMIConnection;
import it.polimi.se2018.utils.network.RMIInterfaceRemote;
import it.polimi.se2018.server.ServerNetwork;

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

    /**
     * Add a client to the server connection list
     * @param rmiInterfaceRemote client remote object reference
     * @return   if the server has accepted the client
     * @throws RemoteException if a rmi error occours
     */
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
