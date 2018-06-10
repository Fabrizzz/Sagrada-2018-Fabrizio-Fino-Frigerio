package it.polimi.se2018.client;

import it.polimi.se2018.View.View;
import it.polimi.se2018.server.rmi.ServerRMIConnection;
import it.polimi.se2018.server.rmi.ServerRMIInterface;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.NetworkHandler;
import it.polimi.se2018.utils.network.RMIInterfaceRemote;
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
    private View view;
    private Connection connection;

    /**
     * Constructor
     * @param view client view
     */
    public ClientNetwork(View view){
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
        if(!isConnected()) {
            try {
                socket = new Socket(hostname, port);
                connection = new SocketConnection(this, socket);
                connection.addObserver(view);
                (new Thread((SocketConnection) connection)).start();
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
    public Boolean connectRMI(String hostname){
        if(!isConnected()){
            try {
                ServerRMIInterface serverRMIInterface = (ServerRMIInterface)Naming.lookup("//".concat(hostname.concat("/MyServer")));

                ClientRMIImplementationRemote connectionIn = new ClientRMIImplementationRemote(this);

                RMIInterfaceRemote remoteRef = (RMIInterfaceRemote) UnicastRemoteObject.exportObject((Remote) connectionIn, 0);

                RMIInterfaceRemote serverRMIInterfaceRemote = serverRMIInterface.addClient(remoteRef);

                if(serverRMIInterfaceRemote != null){
                    connection = new ServerRMIConnection(serverRMIInterfaceRemote);
                    connectionIn.addObserver(view);
                    view.addObserver(connection);
                    return true;
                }else{
                    return true;
                }

            } catch (MalformedURLException | RemoteException | NotBoundException e) {
               e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }

    }

    /**
     * Send a message to the server
     * @param message message to send
     * @return if the message has been sent correctly
     */
    public Boolean sendMessage(Message message){
        if(isConnected()){
            return connection.sendMessage(message);
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
     * Remove the current connection and notify the view
     * @param connection
     */
    public void closeConnection(Object connection) {
        this.connection = null;
        view.connectionClosed();
    }

}
