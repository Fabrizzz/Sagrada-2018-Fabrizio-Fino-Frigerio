package it.polimi.se2018.server;

import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Gestore connessione client socket in entrata
 * @author Alessio
 */
public class SocketConnectionGatherer extends Thread {

    private ServerNetwork serverNetwork;
    private int port;
    private ServerSocket serverSocket;
    private Boolean run = true;

    /**
     * Costruttore
     * @param serverNetwork server
     * @param port porta su cui ricevere le connessioni
     */
    public SocketConnectionGatherer(ServerNetwork serverNetwork, int port){
        this.serverNetwork = serverNetwork;
        this.port = port;

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
            Connection client;
            try {

                clientSocket = serverSocket.accept();
                client = new SocketConnection(serverNetwork,clientSocket);
                serverNetwork.addClient(client);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
