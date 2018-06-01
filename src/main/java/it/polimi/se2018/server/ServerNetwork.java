package it.polimi.se2018.server;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.server.rmi.ServerRMIImplementation;
import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.enums.MessageType;
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
    private ArrayList<Connection> waitingInitializationList = new ArrayList<>();
    private ArrayList<Player> playerList = new ArrayList<>();

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
    public boolean addClient(Connection clientConnection){
        if(waitingInitializationList.size() < 4 && lobbyWaiting){
            waitingInitializationList.add(clientConnection);

            if(connectionMap.size() >= 2){
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        lobbyWaiting = false;
                        initializeGame();
                    }
                }, (long) 60*1000);
            }
            return true;
        }

        return false;
    }

    public void initializeGame(){
        for(int i = 0; i < waitingInitializationList.size(); i ++){
            waitingInitializationList.get(i).close();
            waitingInitializationList.remove(i);
        }

        //Crea model??
    }

    public RemoteView initializeConnection(Connection connection,Message message){
        if(waitingInitializationList.contains(connection) && message.getMessageType() == MessageType.INITIALCONFIG){
            playerList.add(new Player(((ClientMessage) message).getNick(),((ClientMessage) message).getId()));
            RemoteView remoteView = new RemoteView(playerList.get(playerList.size()-1));
            connectionMap.put(((ClientMessage) message).getId(),connection);
            remoteMap.put(connection,remoteView);
            remoteView.addObserver(connection);
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
        remoteMap.get(connection).deleteObservers();
    }
}
