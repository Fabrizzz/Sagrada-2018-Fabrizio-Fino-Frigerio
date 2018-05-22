package it.polimi.se2018.client;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.rmi.client.ClientRMIImplementation;
import it.polimi.se2018.rmi.server.ServerRMIImplementation;
import it.polimi.se2018.rmi.server.ServerRMIInterface;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.NetworkHandler;
import it.polimi.se2018.utils.network.SocketConnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Client connection manager
 * @author Alessio
 */
public class ClientNetwork implements NetworkHandler {
    private Controller controller;
    private Connection connection;

    /**
     * Constructor
     * @param controller controller
     */
    public ClientNetwork(Controller controller){
        this.controller = controller;
    }

    /**
     * Connect to the server using a socket connection
     * @param hostname hostname server
     * @param port server port
     * @return true if the connection can be created, fase otherwise
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
     * Connect to the server using rmi
     * @param hostname hostname del server
     * @return true if the connection can be created, false otherwise
     */
    public boolean connectRMI(String hostname){
        if(!isConnected()){
            ServerRMIInterface server;
            try {
                server = (ServerRMIInterface)Naming.lookup("//".concat(hostname.concat("/MyServer")));

                connection = new ClientRMIImplementation();

                ClientRMIImplementation remoteRef = (ClientRMIImplementation) UnicastRemoteObject.exportObject((Remote) connection, 0);

                return server.addClient(remoteRef);

            } catch (MalformedURLException | RemoteException | NotBoundException e) {
                System.out.println(e);
                return false;
            }
        }else{
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
            return true;
        }
    }

    /**
     * Called by the connection when a message is recived from the server
     * @param message message recived
     * @param connection connection
     */
    public void reciveMessage(Message message, Connection connection) {
        //notifica controller
    }

    /**
     * Send a message to the server
     * @param message message to send
     * @return true if the message has been sent correctly
     */
    public boolean sendMessage(Message message){
        if(isConnected()){
            return connection.sendMessage(message);
        }else{
            return false;
        }
    }

    /**
     * Remove the connection
     * @param connection connection
     */
    public void removeConnection(Connection connection) {
        this.connection = null;
    }

}
