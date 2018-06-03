package it.polimi.se2018.server.rmi;

import it.polimi.se2018.utils.network.RMIInterfaceRemote;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;

import java.rmi.RemoteException;
import java.util.Observable;

public class ServerRMIConnection extends Connection {
    private RMIInterfaceRemote serverRMIInterfaceRemote;

    public ServerRMIConnection(RMIInterfaceRemote serverRMIInterfaceRemote){
        this.serverRMIInterfaceRemote = serverRMIInterfaceRemote;
    }

    public boolean sendMessage(Message message) {
        try{
            return serverRMIInterfaceRemote.sendMessage(message);
        }catch (RemoteException e){
            return false;
        }
    }

    public boolean isConnected() {
        try{
            return serverRMIInterfaceRemote.isConnected();
        }catch (RemoteException e){
            return false;
        }
    }

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
