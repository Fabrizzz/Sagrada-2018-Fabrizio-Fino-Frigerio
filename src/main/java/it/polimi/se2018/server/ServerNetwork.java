package it.polimi.se2018.server;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.server.rmi.ServerRMIController;
import it.polimi.se2018.server.rmi.ServerRMIControllerInterface;
import it.polimi.se2018.server.socket.SocketConnectionGatherer;
import it.polimi.se2018.utils.JSONUtils;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.MessageType;
import it.polimi.se2018.utils.exceptions.DisconnectedException;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.ServerMessage;
import it.polimi.se2018.utils.network.Connection;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the connections with the clients
 * @author Alessio
 */
public class ServerNetwork implements Observer {
    private static final Logger LOGGER = Logger.getLogger("Logger");

    private Map<Long, RemoteView> playingConnections = new HashMap<>();
    private Map<Long, RemoteView> waitingConnections = new HashMap<>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Timer timer = new Timer();
    private int games = 0;

    public void start(int port) {
        LOGGER.log(Level.FINE,"avvio");
        executor.submit(new SocketConnectionGatherer(this, port));

        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE,"Errore creazione registry, porta gia' in uso o rmiregistry non avviato");
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
        Controller controller = new Controller(this);
        ArrayList<RemoteView> views = new ArrayList<>(waitingConnections.values());
        new Thread(() -> controller.startGame(new ArrayList<>(views))).start();
        playingConnections.putAll(waitingConnections);
        waitingConnections.clear();
        games++;
    }

    private synchronized int getGames() {
        return games;
    }

    public synchronized void timerScaduto(int games) {
        System.out.println("Timer scaduto");
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
        if (waitingConnections.values().stream().map(k -> k.getPlayer().getNick()).anyMatch(k -> k.equals(message.getNick()))) {
            LOGGER.log(Level.FINE, "Connessione rifiutata, nick ripetuto");
            try {
                connection.sendMessage(new ServerMessage(ErrorType.CONNECTIONREFUSED));
            } catch (DisconnectedException e) {
                LOGGER.log(Level.WARNING,"Errore invio messaggio connessione rifiutata");
            }
        } else if (playingConnections.containsKey(message.getId())) {
            playingConnections.get(message.getId()).changeConnection(connection);
        } else {
            RemoteView remoteView = new RemoteView(new Player(message.getNick(), message.getId()), connection);
            waitingConnections.put(message.getId(), remoteView);
            if (waitingConnections.size() == 2) {
                LOGGER.log(Level.FINE,"Avviato timer avvio partita, tempo rimanente 60s");
                timer.schedule(new ConnectionTimer(this, getGames()), (long) JSONUtils.readConnectionTimer() * 1000);
            } else if (waitingConnections.size() == 4) {
                LOGGER.log(Level.FINE,"Numero massimo di giocatori raggiunto, avvio inizializzazione");
                initializeGame();
            }
        }




    }

    /**
     * Deregister all the RemoteViews
     *
     * @param views remoteviews to deregister
     */

    public synchronized void deregisterConnections(List<RemoteView> views) {
        while (!views.isEmpty()) {
            for (Long id : playingConnections.keySet()) {
                if (views.contains(playingConnections.get(id))) {
                    views.remove(playingConnections.get(id));
                    playingConnections.remove(id);
                    break;
                }
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
