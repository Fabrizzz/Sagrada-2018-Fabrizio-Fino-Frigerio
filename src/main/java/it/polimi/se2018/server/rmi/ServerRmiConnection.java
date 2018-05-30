package it.polimi.se2018.server.rmi;

import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;

import java.rmi.Remote;
import java.util.Observable;

public class ServerRmiConnection extends Connection implements Remote {

    private ServerNetwork serverNetwork;
    private Boolean connected = true;

    public ServerRmiConnection(ServerNetwork serverNetwork){
        this.serverNetwork = serverNetwork;
    }

    public boolean sendMessage(Message message) {
        if(isConnected()){
            setChanged();
            notifyObservers(message);
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
        serverNetwork.closeConnection(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        sendMessage((Message) arg);
    }
}
