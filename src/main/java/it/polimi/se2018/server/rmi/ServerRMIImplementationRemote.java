package it.polimi.se2018.server.rmi;

import it.polimi.se2018.client.ClientRMIConnection;
import it.polimi.se2018.utils.network.RMIInterfaceRemote;
import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.enums.MessageType;

import java.util.Observable;

public class ServerRMIImplementationRemote extends Observable implements RMIInterfaceRemote {

    private ServerNetwork serverNetwork;
    private ClientRMIConnection clientRMIConnection;
    private Boolean connected = true;

    public ServerRMIImplementationRemote(ServerNetwork serverNetwork, ClientRMIConnection clientRMIConnection){
        this.serverNetwork = serverNetwork;
        this.clientRMIConnection = clientRMIConnection;
    }

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

    public boolean isConnected()  {
        return connected;
    }

    public void close() {
        connected = false;
        this.deleteObservers();
        serverNetwork.closeConnection(this);
    }

    public void update(Observable o, Object arg) {
        sendMessage((Message) arg);
    }
}
