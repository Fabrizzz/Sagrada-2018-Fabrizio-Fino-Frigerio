package it.polimi.se2018.utils.network;

import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.Message;

import java.util.Observable;
import java.util.Observer;

/**
 * Connection interface, implemented by SocketConnection and RMIconnection
 * @author Alessio
 */
public abstract class Connection extends Observable implements Observer {

    public abstract boolean sendMessage(Message message);

    public abstract boolean isConnected();

    public abstract void close();
}
