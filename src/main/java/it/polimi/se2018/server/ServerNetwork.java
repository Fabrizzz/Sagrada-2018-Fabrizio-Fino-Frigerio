package it.polimi.se2018.server;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.server.rmi.ServerRMIImplementation;
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
public class ServerNetwork extends Observable implements NetworkHandler {
    private SocketConnectionGatherer connectionGatherer;
    private ServerRMIImplementation serverRMIImplementation;
    private boolean lobbyWaiting = true;
    private Map<Long,Connection> connectionMap = new HashMap<>();
    private Map<Connection,RemoteView> remoteMap = new HashMap<>();

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
    }

    /**
     * Add a connection to the connection map
     * @param clientConnection connection to add
     */
    public RemoteView addClient(Connection clientConnection){
        if(connectionMap.size() < 4 && lobbyWaiting){
            RemoteView remoteView = new RemoteView();
            clientConnection.addObserver(remoteView);
            remoteMap.put(clientConnection,remoteView);

            if(connectionMap.size() >= 2){
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        lobbyWaiting = false;
                    }
                }, (long) 60*1000);
            }
            return remoteView;
        }else{
            return null;
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

    public void closeConnection(Connection connection){
        //chiudi connessione
    }
}
