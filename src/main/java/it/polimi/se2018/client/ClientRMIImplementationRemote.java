package it.polimi.se2018.client;

import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.RMIInterfaceRemote;

import java.util.Observable;

/**
 * Remote class of the client
 * @author Alessio
 */
public class ClientRMIImplementationRemote extends Observable implements RMIInterfaceRemote {

    private ClientNetwork clientNetwork;
    private Boolean connected = true;

    /**
     * Costructor
     * @param clientNetwork client network manager
     */
    public ClientRMIImplementationRemote(ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
    }

    /**
     * Method called by the server to send a message to the client
     * @param message message sent
     * @return if the message has been correctly recived
     */
    public boolean sendMessage(Message message){
        if(connected){
            setChanged();
            notifyObservers(message);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Return the status of the connection
     * @return the status of the connection
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Close the connection
     */
    public void close(){
        this.connected = false;
        this.deleteObservers();
        clientNetwork.closeConnection(this);
    }

    public void update(Observable o, Object arg) {
        sendMessage((Message) arg);
    }
}
