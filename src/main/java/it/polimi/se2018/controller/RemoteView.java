package it.polimi.se2018.controller;

import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.enums.MessageType;
import it.polimi.se2018.utils.exceptions.DisconnectedException;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.Message;
import it.polimi.se2018.utils.messages.ServerMessage;
import it.polimi.se2018.utils.network.Connection;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteView extends Observable implements Observer {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private Player player;

    private Connection connection;

    public RemoteView(Player player, Connection connection) {
        LOGGER.log(Level.FINE,"RemoteView creata");
        this.player = player;
        this.connection = connection;
        connection.addObserver(new MessageReceiver());

    }

    public void changeConnection(Connection connection) {
        LOGGER.log(Level.FINE,"Connessione cambia");
        this.connection = connection;
    }


    public Player getPlayer() {
        return player;
    }

    public void sendBack(Message message) {
        LOGGER.log(Level.FINE, "Invio messaggio");
        try {
            connection.sendMessage(message);
        } catch (DisconnectedException e) {
            setChanged();
            notifyObservers(new ClientMessage(player.getNick()));
            LOGGER.log(Level.WARNING, player.getNick() + " si è disconnesso");
        }

    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    @Override
    public void update(Observable o, Object arg) {
        LOGGER.log(Level.FINE,"Ricevuto nuovo modelview");

        ModelView modelView = (ModelView) arg;

        sendBack(new ServerMessage(MessageType.MODELVIEWUPDATE, modelView));
    }

    public void process(ClientMessage message) {
        setChanged();
        notifyObservers(message);
    }


    private class MessageReceiver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            LOGGER.log(Level.INFO,"Messaggio ricevuto dal MessageReciver remoteView");
            process((ClientMessage) arg);

        }

    }

}
