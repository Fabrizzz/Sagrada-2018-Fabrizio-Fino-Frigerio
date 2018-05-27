package it.polimi.se2018.server;

import it.polimi.se2018.client.ClientRMIImplementation;
import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;

import java.rmi.RemoteException;

/**
 * RMI connection with the client
 * @author Alessio
 */
public class RMIConnection implements Connection {
    private ClientRMIImplementation rmiImplementation;
    private boolean connected = true;

    /**
     * Constructor
     * @param clientRMIImplementation client implementation
     */
    public RMIConnection(ClientRMIImplementation clientRMIImplementation){
        this.rmiImplementation = clientRMIImplementation;
    }
    /**
     *  Send a message to the client
     * @param message message to send
     * @return true if the message was sent correctly, false otherwise
     */
    public boolean sendMessage(Message message) {
        if(isConnected()){
            try{
                rmiImplementation.sendMessage(message);
            }catch (RemoteException e){
                connected = false;
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Return if the connection is active
     * @return status of the connection
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Wait for the initializzation message
     * @return the message recived
     */
    public ClientMessage waitInitializationMessage(){
        return null;
    }

    /**
     * Close the connection
     */
    public void close(){
        try {
            this.rmiImplementation.close();
        }catch(RemoteException e){}
        this.connected = false;
    }
}
