package it.polimi.se2018.socket.server;

import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gestore connessione client socket in entrata
 * @author Alessio
 */
public class SocketConnectionGatherer extends Thread {

    private ServerNetwork serverNetwork;
    private int port;
    private ServerSocket serverSocket;

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

    @Override
    public void run() {
        while(true) {

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