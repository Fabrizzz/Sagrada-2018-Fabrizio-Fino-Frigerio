package it.polimi.se2018.server;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.rmi.server.ServerRMIImplementation;
import it.polimi.se2018.socket.server.SocketConnectionGatherer;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.NetworkHandler;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

/**
 * Riceve e invia messaggi ai client
 * @author Alessio
 */
public class ServerNetwork implements NetworkHandler {
    private ArrayList<Connection> clientConnections = new ArrayList<Connection>();
    private SocketConnectionGatherer connectionGatherer;
    private RemoteView remoteView;
    private ServerRMIImplementation serverRMIImplementation;

    /**
     * Costruttore
     * @param remoteView remoteView del server
     */
    public ServerNetwork(RemoteView remoteView){
        this.remoteView = remoteView;
        connectionGatherer = new SocketConnectionGatherer(this,8421);
        connectionGatherer.start();

        try {
            LocateRegistry.createRegistry(8422);
        } catch (RemoteException e) {}

        try {
            serverRMIImplementation = new ServerRMIImplementation(this);

            Naming.rebind("//localhost/MyServer", serverRMIImplementation);
        } catch (MalformedURLException e) {
            //System.err.println("Impossibile registrare l'oggetto indicato!");
        } catch (RemoteException e) {
            //System.err.println("Errore di connessione: " + e.getMessage() + "!");
        }
    }

    /**
     * Aggiunge un client alla lista delle connessioni, puo' essere sia un socket o rmi client
     * @param clientConnection client da aggiungere
     */
    public boolean addClient(Connection clientConnection){
        if(clientConnections.size() <= 4){
            clientConnections.add(clientConnection);
            return true;
        }else{
            return false;
        }

    }

    /**
     * Rimuove un client dalla lista delle connessioni
     * @param clientConnection client da rimuovere
     */
    public void removeConnection(Connection clientConnection){
        clientConnections.remove(clientConnection);
    }

    /**
     * Invia un messaggio ad un client
     * @param message messaggio
     * @param connection connessione al client
     */
    public boolean sendMessage(Message message, Connection connection){
        if(clientConnections.contains(connection)){
            return connection.sendMessage(message);
        }else{
            return false;
        }
    }

    /**
     * Invia un messaggio a tutti i client
     * @param message messaggio
     */
    public void sendAll(Message message){
        for(Connection connection : clientConnections){
            connection.sendMessage(message);
        }
    }

    /**
     * Metodo invocato dai socket o dalle rmi quando e' stato ricevuto un messaggio da un client
     * @param message messaggio ricevuto dal client
     * @param clientConnection client connection che ha ricevuto il messaggio
     */
    public void reciveMessage(Message message,Connection clientConnection){
        //notifica remoteView
    }

}
