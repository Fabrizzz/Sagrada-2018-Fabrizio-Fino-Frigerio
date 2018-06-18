package it.polimi.se2018.client;

import it.polimi.se2018.view.View;
import it.polimi.se2018.server.rmi.ServerRMIControllerInterface;
import it.polimi.se2018.utils.messages.Message;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.RMIConnection;
import it.polimi.se2018.utils.network.RMIInterface;
import it.polimi.se2018.utils.network.SocketConnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client connection manager
 * @author Alessio
 */
public class ClientNetwork {
    private View view;
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger("Logger");

    /**
     * Constructor
     * @param view client view
     */
    public ClientNetwork(View view){
        LOGGER.log(Level.FINE,"Avvio");
        this.view = view;
    }

    /**
     * Connect to the server using a socket connection
     * @param hostname hostname server
     * @param port server port
     * @return true if the connection can be created, fase otherwise
     */
    public Boolean connectSocket(String hostname, int port){
        Socket socket;
        LOGGER.log(Level.INFO,"Tentativo di connessione a " +  hostname +":" + port);
        if(!isConnected()) {
            try {
                socket = new Socket(hostname, port);
                connection = new SocketConnection(socket);
                connection.addObserver(view);
                view.addObserver(connection);
                (new Thread((SocketConnection) connection)).start();
                LOGGER.log(Level.INFO,"Connessione accettata");
                return true;
            } catch (IOException e) {
                LOGGER.log(Level.INFO,"Errore connessione, connessione rifiutata");
                return false;
            }
        }else{
            LOGGER.log(Level.INFO,"Client gia connesso, tentativo di connessione annullato");
            return false;
        }
    }

    /**
     * Connect to the server using rmi
     * @param hostname hostname del server
     * @return true if the connection can be created, false otherwise
     */
    public Boolean connectRMI(String hostname){
        LOGGER.log(Level.INFO,"Tentativo di connessione a " +  hostname);
        if(!isConnected()){
            try {
                ServerRMIControllerInterface serverRMIControllerInterface = (ServerRMIControllerInterface) Naming.lookup("//".concat(hostname.concat("/MyServer")));

                RMIConnection rmiConnection = new RMIConnection();

                RMIInterface remoteRef = (RMIInterface) UnicastRemoteObject.exportObject(rmiConnection, 0);

                RMIInterface serverRMIInterface = serverRMIControllerInterface.addClient(remoteRef);

                rmiConnection.setRmiInterface(serverRMIInterface);

                connection = rmiConnection;

                if (serverRMIInterface != null) {
                    connection.addObserver(view);
                    view.addObserver(connection);
                    LOGGER.log(Level.INFO,"Connessione accettata");
                    return true;
                }else{
                    LOGGER.log(Level.INFO,"Connessione rifiutata");
                    return false;
                }

            } catch (MalformedURLException | RemoteException | NotBoundException e) {
                LOGGER.log(Level.SEVERE,"Errore connessione rmi");
                return false;
            }
        }else{
            LOGGER.log(Level.INFO,"Client gia connesso, tentativo di connessione annullato");
            return false;
        }

    }

    /**
     * Send a message to the server
     * @param message message to send
     * @return if the message has been sent correctly
     */
    public Boolean sendMessage(Message message){
        LOGGER.log(Level.FINE,"Invio messaggio");
        if(isConnected()){
            return connection.sendMessage(message);
        }else{
            LOGGER.log(Level.FINE,"Client non connesso, invio annullato");
            return false;
        }
    }

    /**
     * Return the status of the connection to the server
     * @return true if a connection exist and i active, false otherwise
     */
    public boolean isConnected(){
        if(connection == null){
            return false;
        }else{
            return connection.isConnected();
        }
    }

    /**
     * Remove the current connection and notify the view
     * @param connection
     */
    public void closeConnection(Object connection) {
        LOGGER.log(Level.INFO,"Chiusura connessione");
        this.connection = null;
        view.connectionClosed();
    }

}
