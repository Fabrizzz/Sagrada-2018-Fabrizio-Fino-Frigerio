package it.polimi.se2018.client;

import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.RMIInterfaceRemote;

import java.rmi.RemoteException;
import java.util.Observable;

/**
 * Class rapresenting the rmi connection from the server to the client
 * @author Alessio
 */
public class ClientRMIConnection extends Connection {

    private RMIInterfaceRemote clientRMIImplementation;

    /**
     * Costructor
     * @param clientRMIInterface remote rmi object of the client
     */
    public ClientRMIConnection(RMIInterfaceRemote clientRMIInterface){
        this.clientRMIImplementation = clientRMIInterface;
    }

    /**
     * Send a maessage to the client
     * @param message message to send
     * @return if the message has been correctly sent
     */
    public boolean sendMessage(Message message) {
        try {
            return clientRMIImplementation.sendMessage(message);
        }catch (RemoteException e){
            return false;
        }
    }

    /**
     * Return the status of the connection
     * @return if the connection is still active
     */
    public boolean isConnected() {
        try{
            return clientRMIImplementation.isConnected();
        }catch (RemoteException e){
            return false;
        }
    }

    /**
     * Close the connection
     */
    public void close() {
        try {
            clientRMIImplementation.close();
        }catch (RemoteException e){
            return;
        }
    }


    public void update(Observable o, Object arg) {
        try{
            clientRMIImplementation.update(o,arg);
        }catch (RemoteException e){
            return;
        }
    }
}
