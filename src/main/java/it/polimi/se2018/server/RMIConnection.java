package it.polimi.se2018.server;

import it.polimi.se2018.client.ClientRMIImplementation;
import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;

import java.rmi.RemoteException;

public class RMIConnection implements Connection {
    private ClientRMIImplementation rmiImplementation;
    private boolean connected = true;

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

    public boolean isConnected() {
        return connected;
    }

    public ClientMessage waitInitializationMessage(){
        return null;
    }

    public void close(){
        try {
            this.rmiImplementation.close();
        }catch(RemoteException e){}
        this.connected = false;
    }
}
