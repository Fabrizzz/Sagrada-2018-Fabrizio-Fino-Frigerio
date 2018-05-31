package it.polimi.se2018.client;

import it.polimi.se2018.View.View;
import it.polimi.se2018.server.rmi.ServerRMIInterface;
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
    private View view;
    private Connection connection;


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

                Connection connectionIn = new ClientRMIImplementation(this);

                ClientRMIImplementation remoteRef = (ClientRMIImplementation) UnicastRemoteObject.exportObject((Remote) connectionIn, 0);

                connection = serverRMIInterface.addClient(remoteRef);

                if(connection != null){
                    connectionIn.addObserver(view);
                    view.addObserver(connection);
                    return true;
                }else{
                    return true;
                }

            } catch (MalformedURLException | RemoteException | NotBoundException e) {
                System.out.println(e);
                return false;
            }
        }else{
            return false;
        }

    }

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

    public void closeConnection(Connection connection) {
        this.connection = null;
    }

}
