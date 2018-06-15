package it.polimi.se2018.server;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.server.rmi.ServerRMIController;
import it.polimi.se2018.server.rmi.ServerRMIControllerInterface;
import it.polimi.se2018.server.socket.SocketConnectionGatherer;
import it.polimi.se2018.utils.enums.MessageType;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.network.Connection;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the connections with the clients
 * @author Alessio
 */
public class ServerNetwork implements Observer {
    private static final Logger LOGGER = Logger.getLogger( ServerNetwork.class.getName() );
    private Map<Long, RemoteView> playingConnections = new HashMap<>();
    private Map<Long, RemoteView> waitingConnections = new HashMap<>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Timer timer = new Timer();
    private int games = 0;

    public void start(int port) {
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.ALL);
        LOGGER.addHandler(handlerObj);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
        LOGGER.log(Level.FINE,"avvio");
        executor.submit(new SocketConnectionGatherer(this, port));

        try {
            LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE,"Errore creazione registry");
        }

        try {
            ServerRMIControllerInterface serverRMIImplementation = new ServerRMIController(this);
            Naming.rebind("//localhost/MyServer", serverRMIImplementation);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE,"Impossibile registrare l'oggetto indicato!");
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE,"Errore inizializzazione rmi");
        }
    }

    /**
     * Start the game and remove the connection still waiting for initializzation
     */
    private synchronized void initializeGame() {
        Controller controller = new Controller();
        controller.startGame(waitingConnections.values());
        playingConnections.putAll(waitingConnections);
        waitingConnections.clear();
        games++;
    }

    private synchronized int getGames() {
        return games;
    }

    public synchronized void timerScaduto(int games) {
        if (getGames() == games) {
            LOGGER.log(Level.FINE,"Timer scaduto, inizializzazione gioco");
            initializeGame();
        }

    }

    /**
     * Initialize a connection with the nickname and id recive from the client
     * @param connection connection to initialize
     * @param message initializzation message recived from the client
     */
    public synchronized void initializeConnection(Connection connection, ClientMessage message) {
        LOGGER.log( Level.FINE, "initialize connection con message id: " + message.getId() + " e nickname " + message.getNick());
        if (playingConnections.containsKey(message.getId())) {
            playingConnections.get(message.getId()).changeConnection(connection);
        } else {
            RemoteView remoteView = new RemoteView(new Player(message.getNick(), message.getId()), connection);
            waitingConnections.put(message.getId(), remoteView);
            if (waitingConnections.size() == 2) {
                timer.schedule(new ConnectionTimer(this, getGames()), (long) 60 * 1000);
            } else if (waitingConnections.size() == 4) {
                initializeGame();
            }
        }




    }

    @Override
    public void update(Observable o, Object arg) {
        LOGGER.log( Level.FINE, "messaggio ricevuto");
        Connection connection = (Connection) o;
        ClientMessage message = (ClientMessage) arg;
        if (message.getMessageType().equals(MessageType.INITIALCONFIG)) {
            LOGGER.log( Level.FINE, "Ricevuto messaggio INITIALCONFIG");
            initializeConnection(connection, message);
            o.deleteObserver(this);
        }
    }
}
