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

/**
 * Manages the connections with the clients
 * @author Alessio
 */
public class ServerNetwork implements Observer {
    private Map<Long, RemoteView> playingConnections = new HashMap<>();
    private Map<Long, RemoteView> waitingConnections = new HashMap<>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Timer timer = new Timer();
    private int games = 0;


    public void start(int port) {
        executor.submit(new SocketConnectionGatherer(this, port));

        try {
            LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {}

        try {
            ServerRMIControllerInterface serverRMIImplementation = new ServerRMIController(this);
            Naming.rebind("//localhost/MyServer", serverRMIImplementation);
        } catch (MalformedURLException e) {
            System.err.println("Impossibile registrare l'oggetto indicato!");
        } catch (RemoteException e) {
            e.printStackTrace();
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
            System.out.println("Timer scaduto, inizializzazione gioco");
            initializeGame();
        }

    }

    /**
     * Initialize a connection with the nickname and id recive from the client
     * @param connection connection to initialize
     * @param message initializzation message recived from the client
     */
    public synchronized void initializeConnection(Connection connection, ClientMessage message) {

        if (playingConnections.containsKey(message.getId())) {
            playingConnections.get(message.getId()).changeConnection(connection);
        } else {
            RemoteView remoteView = new RemoteView(new Player(message.getNick(), message.getId()), connection);
            waitingConnections.put(message.getId(), remoteView);
            if (waitingConnections.size() == 2) {
                timer.schedule(new ConnectionTimer(this, getGames()), (long) 60 * 1000);
            } else if (waitingConnections.size() == 4) {
                System.out.println("Inizializzazione gioco");
                initializeGame();
            }
        }




    }

    @Override
    public void update(Observable o, Object arg) {
        Connection connection = (Connection) o;
        ClientMessage message = (ClientMessage) arg;
        if (message.getMessageType().equals(MessageType.INITIALCONFIG)) {
            initializeConnection(connection, message);
            o.deleteObserver(this);
        }
    }
}
