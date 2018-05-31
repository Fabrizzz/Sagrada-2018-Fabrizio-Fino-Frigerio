package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.Message;

/**
 * Network manager interface
 * @author Alessio
 */
public interface NetworkHandler {

    //public void reciveMessage(Message message,Connection connection);
    public void closeConnection(Connection connection);
}
