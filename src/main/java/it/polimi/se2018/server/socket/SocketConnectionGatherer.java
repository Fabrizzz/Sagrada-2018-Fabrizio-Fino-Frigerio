package it.polimi.se2018.server.socket;

import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket connection gatherer
 * @author Alessio
 */
public class SocketConnectionGatherer implements Runnable {

    private ServerNetwork serverNetwork;
    private ServerSocket serverSocket;
    private Boolean run = true;

    /**
     * Costructor
     * @param serverNetwork server network manager
     * @param port listening port
     */
    public SocketConnectionGatherer(ServerNetwork serverNetwork, int port){
        this.serverNetwork = serverNetwork;

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop listening for connections
     */
    public void terminate(){
        try {
            serverSocket.close();
        }catch (IOException e) {}

        run = false;
    }

    @Override
    public void run() {
        while(run) {

            Socket clientSocket;
            SocketConnection connection;
            try {

                clientSocket = serverSocket.accept();
                connection = new SocketConnection(clientSocket);
                (new Thread(connection)).start();
                connection.addObserver(serverNetwork);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
