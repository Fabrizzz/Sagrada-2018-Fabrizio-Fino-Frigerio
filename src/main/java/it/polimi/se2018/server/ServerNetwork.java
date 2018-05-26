package it.polimi.se2018.server;

import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.NetworkHandler;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;

/**
 * Manages the connections with the clients
 * @author Alessio
 */
public class ServerNetwork implements NetworkHandler {
    private SocketConnectionGatherer connectionGatherer;
    private ServerRMIImplementation serverRMIImplementation;
    private boolean lobbyWaiting = true;
    private Map<Long,Connection> connectionMap = new HashMap<>();

    /**
     * Costructor
     */
    public ServerNetwork(){
        connectionGatherer = new SocketConnectionGatherer(this,8421);
        connectionGatherer.start();

        try {
            LocateRegistry.createRegistry(8422);
        } catch (RemoteException e) {}

        try {
            serverRMIImplementation = new ServerRMIImplementation(this);

            Naming.rebind("//localhost/MyServer", serverRMIImplementation);
        } catch (MalformedURLException e) {
            System.err.println("Impossibile registrare l'oggetto indicato!");
        } catch (RemoteException e) {
            //System.err.println("Errore di connessione: " + e.getMessage() + "!");
            e.printStackTrace();
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                lobbyWaiting = false;
            }
        }, 60*1000);
    }

    /**
     * Add a connection to the connection map
     * @param clientConnection connection to add
     */
    public boolean addClient(Connection clientConnection){
        if(connectionMap.size() <= 4 && lobbyWaiting){
            ClientMessage clientMessage;
            clientMessage = clientConnection.waitInitializationMessage();
            if(clientMessage != null){
                connectionMap.put(clientMessage.getId(),clientConnection);
                return true;
            }
        }else if(connectionMap.size() <= 4 && !lobbyWaiting) {
            ClientMessage clientMessage;
            clientMessage = clientConnection.waitInitializationMessage();
            if(connectionMap.get(clientMessage.getId()) != null){
                if(!connectionMap.get(clientMessage.getId()).isConnected()){
                    connectionMap.put(clientMessage.getId(),clientConnection);
                    return true;
                }
            }
        }
        clientConnection.close();
        return false;

    }


    /**
     * Send a message to the client
     * @param message message
     * @param connection client connection
     */
    public boolean sendMessage(Message message, Connection connection){
        if(connectionMap.containsValue(connection) && connection.isConnected()){
            return connection.sendMessage(message);
        }else{
            return false;
        }
    }

    /**
     * Send a message to all the connected client
     * @param message message
     */
    public void sendAll(Message message){
        for(Connection connection : connectionMap.values()){
            if(connection.isConnected()){
                connection.sendMessage(message);
            }
        }
    }

    /**
     * Method called when a message is recived from a client
     * @param message message recived
     * @param clientConnection connection
     */
    public void reciveMessage(Message message,Connection clientConnection){
        //notifica remoteView
    }

}
