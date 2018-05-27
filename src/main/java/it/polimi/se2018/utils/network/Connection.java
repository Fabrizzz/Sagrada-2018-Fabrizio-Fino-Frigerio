package it.polimi.se2018.utils.network;

import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.Message;

import java.util.Observable;

/**
 * Connection interface, implemented by SocketConnection and RMIconnection
 * @author Alessio
 */
public interface Connection {

    public boolean sendMessage(Message message);

    public ClientMessage waitInitializationMessage();

    public boolean isConnected();

    public void close();
}
