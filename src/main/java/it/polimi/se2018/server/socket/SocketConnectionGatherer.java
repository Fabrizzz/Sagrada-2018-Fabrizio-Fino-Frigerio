package it.polimi.se2018.server.socket;

import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.network.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Socket connection gatherer
 * @author Alessio
 */
public class SocketConnectionGatherer implements Runnable {
    private static final Logger LOGGER = Logger.getLogger( "Logger");
    private ServerNetwork serverNetwork;
    private ServerSocket serverSocket;
    private Boolean run = true;

    /**
     * Costructor
     * @param serverNetwork server network manager
     * @param port listening port
     */
    public SocketConnectionGatherer(ServerNetwork serverNetwork, int port){
        LOGGER.log(Level.FINE,"avvio");

        this.serverNetwork = serverNetwork;

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Errore, porta di ascolto gia' utilizzata");
        }
    }

    /**
     * Stop listening for connections
     */
    public void terminate(){
        LOGGER.log(Level.INFO,"Server socket terminato");
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
            try{
                clientSocket = serverSocket.accept();
                LOGGER.log(Level.INFO,"Creata nuova connessione socket");
                connection = new SocketConnection(clientSocket);
                (new Thread(connection)).start();
                connection.addObserver(serverNetwork);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE,"Errore creazione connessione socket");
            }

        }
    }
}
