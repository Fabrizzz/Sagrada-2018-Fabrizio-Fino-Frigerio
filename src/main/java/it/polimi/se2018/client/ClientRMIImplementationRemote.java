package it.polimi.se2018.client;

import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

/**
 * Remote class of the client
 * @author Alessio
 */
public class ClientRMIImplementationRemote extends Observable implements RMIInterfaceRemote {

    private ClientNetwork clientNetwork;
    private Boolean connected = true;

    public ClientRMIImplementationRemote(ClientNetwork clientNetwork){
        this.clientNetwork = clientNetwork;
    }

    public boolean sendMessage(Message message){
        if(connected){
            setChanged();
            notifyObservers(message);
            return true;
        }else{
            return false;
        }
    }

    public boolean isConnected() {
        return connected;
    }


    public void close(){
        this.connected = false;
        this.deleteObservers();
        clientNetwork.closeConnection(this);
    }

    public void update(Observable o, Object arg) {
        sendMessage((Message) arg);
    }
}
