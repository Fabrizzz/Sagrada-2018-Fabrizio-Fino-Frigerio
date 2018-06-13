package it.polimi.se2018.client;

import it.polimi.se2018.View.View;
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

/**
 * Client connection manager
 * @author Alessio
 */
public class ClientNetwork {
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
                connection = new SocketConnection(socket);
                connection.addObserver(view);
                view.addObserver(connection);
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
                ServerRMIControllerInterface serverRMIControllerInterface = (ServerRMIControllerInterface) Naming.lookup("//".concat(hostname.concat("/MyServer")));

                RMIConnection rmiConnection = new RMIConnection();

                RMIInterface remoteRef = (RMIInterface) UnicastRemoteObject.exportObject(rmiConnection, 0);

                RMIInterface serverRMIInterface = serverRMIControllerInterface.addClient(remoteRef);

                rmiConnection.setRmiInterface(serverRMIInterface);

                connection = rmiConnection;

                if (serverRMIInterface != null) {
                    connection.addObserver(view);
                    view.addObserver(connection);
                    return true;
                }else{

                    return false;
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
            return connection.isConnected();
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
