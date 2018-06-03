package it.polimi.se2018.client;

import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.RMIInterfaceRemote;

import java.rmi.RemoteException;
import java.util.Observable;

public class ClientRMIConnection extends Connection {

    private RMIInterfaceRemote clientRMIImplementation;

    public ClientRMIConnection(RMIInterfaceRemote clientRMIInterface){
        this.clientRMIImplementation = clientRMIInterface;
    }

    public boolean sendMessage(Message message) {
        try {
            return clientRMIImplementation.sendMessage(message);
        }catch (RemoteException e){
            return false;
        }
    }


    public boolean isConnected() {
        try{
            return clientRMIImplementation.isConnected();
        }catch (RemoteException e){
            return false;
        }
    }


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
