package it.polimi.se2018.server.rmi;

import it.polimi.se2018.client.ClientRMIConnection;
import it.polimi.se2018.utils.network.RMIInterfaceRemote;
import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.enums.MessageType;

import java.util.Observable;

/**
 * Remote class of the server used to recive message from the client
 * @author Alessio
 */
public class ServerRMIImplementationRemote extends Observable implements RMIInterfaceRemote {

    private ServerNetwork serverNetwork;
    private ClientRMIConnection clientRMIConnection;
    private Boolean connected = true;

    /**
     * Constructor
     * @param serverNetwork server network manager
     * @param clientRMIConnection connection from the server to the client
     */
    public ServerRMIImplementationRemote(ServerNetwork serverNetwork, ClientRMIConnection clientRMIConnection){
        this.serverNetwork = serverNetwork;
        this.clientRMIConnection = clientRMIConnection;
    }

    /**
     * Method called by the client to send a message to the server
     * @param message message to send
     * @return if the message has been correctly recived
     */
    public boolean sendMessage(Message message) {
        if(isConnected()){
            if(message.getMessageType() == MessageType.INITIALCONFIG){
                RemoteView remoteView = this.serverNetwork.initializeConnection(clientRMIConnection,message);
                addObserver(remoteView);
            }else {
                setChanged();
                notifyObservers(message);
            }
            return true;
        }else{
            return false;
        }

    }

    /**
     * Return the status of the connection
     * @return the status of the connection
     */
    public boolean isConnected()  {
        return connected;
    }

    /**
     * Close the connection
     */
    public void close() {
        connected = false;
        this.deleteObservers();
        serverNetwork.closeConnection(this);
    }

    public void update(Observable o, Object arg) {
        sendMessage((Message) arg);
    }
}
