package it.polimi.se2018.controller;

import it.polimi.se2018.model.ModelViewUpdate;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.enums.MessageType;
import it.polimi.se2018.utils.exceptions.DisconnectedException;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.ServerMessage;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.view.View;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class acts as a view for the Server and is the only onw to communicate through connection with the client
 * @author Giampietro
 */
public class RemoteView extends View {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private Player player;

    private Connection connection;

    /**
     * Constructor
     * @param player player associated with this remoteview
     * @param connection connection associated with this remoteview
     */

    public RemoteView(Player player, Connection connection) {
        LOGGER.log(Level.FINE,"RemoteView creata");
        this.player = player;
        this.connection = connection;
        connection.addObserver(new MessageReceiver());

    }

    /**
     * It changes the connection reference when a player riconnect
     * @param connection
     */
    public void changeConnection(Connection connection) {
        LOGGER.log(Level.FINE,"Connessione cambia");
        this.connection = connection;
        connection.addObserver(new MessageReceiver());
        setChanged();
        notifyObservers(new ClientMessage(MessageType.HASRICONNECTED));
    }


    public Player getPlayer() {
        return player;
    }

    @Override
    public void connectionClosed() {
        if (connection.isConnected())
            connection.close();
        this.deleteObservers();
    }

    /**
     * It sends a ServerMessage back to the client
     * @param message
     */
    @Override
    public void elaborateMessage(ServerMessage message) {
        LOGGER.log(Level.FINE, "Invio messaggio");
        try {
            connection.sendMessage(message);
        } catch (DisconnectedException e) {
            LOGGER.log(Level.WARNING, player.getNick() + " si è disconnesso");
            setChanged();
            new Thread(() -> notifyObservers(new ClientMessage(MessageType.HASDISCONNECTED))).start();

        }

    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    /**
     * It notifies ClientMessage received from the connection to the controller
     * @param message
     */
    public void process(ClientMessage message) {
        setChanged();
        notifyObservers(message);
    }


    /**
     * It send ModelView Updates back to the client
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        LOGGER.log(Level.FINE,"Ricevuto nuovo modelview");

        ModelViewUpdate update = (ModelViewUpdate) o;

        elaborateMessage(new ServerMessage(update));
    }


    /**
     * it observes the connection and notifies ClientMessages to the controller
     */
    private class MessageReceiver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            LOGGER.log(Level.INFO,"Messaggio ricevuto dal MessageReceiver remoteView");
            process((ClientMessage) arg);

        }

    }

}
