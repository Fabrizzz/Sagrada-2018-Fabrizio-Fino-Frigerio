package it.polimi.se2018.server;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.rmi.server.ServerRMIImplementation;
import it.polimi.se2018.socket.server.ConnectionGatherer;
import it.polimi.se2018.utils.Connection;
import it.polimi.se2018.utils.Message;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

/**
 * Riceve e invia messaggi ai client
 * @author Alessio
 */
public class ServerNetwork{
    private ArrayList<Connection> clientConnections = new ArrayList<Connection>();
    private ConnectionGatherer connectionGatherer;
    private RemoteView remoteView;
    private ServerRMIImplementation serverRMIImplementation;

    /**
     * Costruttore
     * @param remoteView remoteView del server
     */
    public ServerNetwork(RemoteView remoteView){
        this.remoteView = remoteView;
        connectionGatherer = new ConnectionGatherer(this,8421);
        connectionGatherer.run();

        try {
            LocateRegistry.createRegistry(8422);
        } catch (RemoteException e) {}

        try {
            serverRMIImplementation = new ServerRMIImplementation(this);

            Naming.rebind("//localhost/sagradaServer", serverRMIImplementation);
        } catch (MalformedURLException e) {
            System.err.println("Impossibile registrare l'oggetto indicato!");
        } catch (RemoteException e) {
            System.err.println("Errore di connessione: " + e.getMessage() + "!");
        }
    }

    /**
     * Aggiunge un client alla lista delle connessioni, puo' essere sia un socket o rmi client
     * @param clientConnection client da aggiungere
     */
    public void addClient(Connection clientConnection){
        clientConnections.add(clientConnection);
    }

    /**
     * Rimuove un client dalla lista delle connessioni
     * @param clientConnection client da rimuovere
     */
    public void removeClient(Connection clientConnection){
        clientConnections.remove(clientConnection);
    }

    /**
     * Invia un messaggio ad un client
     * @param message messaggio
     * @param connection connessione al client
     */
    public void sendMessage(Message message, Connection connection){
        if(clientConnections.contains(connection)) {
            connection.sendMessage(message);
        }
    }

    /**
     * Invia un messaggio a tutti i client
     * @param message
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

    }

}
