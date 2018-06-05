package it.polimi.se2018.server.rmi;

import it.polimi.se2018.utils.network.RMIInterfaceRemote;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;

import java.rmi.RemoteException;
import java.util.Observable;

/**
 * Class rapresenting the rmi connection from the client to the server
 * @author Alessio
 */
public class ServerRMIConnection extends Connection {
    private RMIInterfaceRemote serverRMIInterfaceRemote;

    /**
     * Costructor
     * @param serverRMIInterfaceRemote the server remote object reference
     */
    public ServerRMIConnection(RMIInterfaceRemote serverRMIInterfaceRemote){
        this.serverRMIInterfaceRemote = serverRMIInterfaceRemote;
    }

    /**
     * Method called by the client to send a message to the server
     * @param message message to send
     * @return if the message has been correctly sent
     */
    public boolean sendMessage(Message message) {
        try{
            return serverRMIInterfaceRemote.sendMessage(message);
        }catch (RemoteException e){
            return false;
        }
    }

    /**
     * Return the status of the connection
     * @return the status of the connection
     */
    public boolean isConnected() {
        try{
            return serverRMIInterfaceRemote.isConnected();
        }catch (RemoteException e){
            return false;
        }
    }

    /**
     * Close the connection
     */
    public void close() {
        try{
            serverRMIInterfaceRemote.close();
        }catch (RemoteException e){
            return;
        }
    }

    public void update(Observable o, Object arg) {
        try{
            serverRMIInterfaceRemote.update(o,arg);
        }catch (RemoteException e){
            return;
        }
    }
}
