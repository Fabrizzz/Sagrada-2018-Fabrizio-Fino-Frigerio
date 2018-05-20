package it.polimi.se2018.client;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.NetworkHandler;
import it.polimi.se2018.utils.network.SocketConnection;

import java.io.IOException;
import java.net.Socket;

/**
 * Gestore connessione client
 * @author Alessio
 */
public class ClientNetwork implements NetworkHandler {
    private Controller controller;
    private Connection connection;

    /**
     * Costruttore
     * @param controller controller
     */
    public ClientNetwork(Controller controller){
        this.controller = controller;
    }

    /**
     * Connetti al server usando un socket
     * @param hostname hostname server
     * @param port porta del server
     * @return true se la connessione viene instaurata, false altrimenti
     */
    public boolean connectSocket(String hostname, int port){
        Socket socket;
        if(!isConnected()) {
            try {
                socket = new Socket(hostname, port);
                connection = new SocketConnection(this, socket);
                return true;
            } catch (IOException e) {
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Ritorna lo stato di connessione del client
     * @return true se esiste una connessione, false altrimenti
     */
    public boolean isConnected(){
        if(connection == null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Metodo richiamato dalla connessione alla ricezione di un messaggio dal server
     * @param message messaggio ricevuto
     * @param connection connessione
     */
    public void reciveMessage(Message message, Connection connection) {
        //notifica controller
    }

    /**
     * Invia messaggio al server
     * @param message messaggio da inviare
     * @return true se l'invio avviene correttamente, false altrimenti
     */
    public boolean sendMessage(Message message){
        if(isConnected()){
            return connection.sendMessage(message);
        }else{
            return false;
        }
    }

    /**
     * Rimuovi la connessione
     * @param connection
     */
    public void removeConnection(Connection connection) {
        this.connection = null;
    }
}
