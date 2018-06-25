package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.exceptions.DisconnectedException;
import it.polimi.se2018.utils.messages.Message;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class rapresenting the rmi connection from the server to the rmiInterface
 * @author Alessio
 */
public class RMIConnection extends Connection implements RMIInterface {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private RMIInterface rmiInterface;
    private boolean isConnected = true;

    /**
     * Costructor
     * @param clientRMIInterface remote rmi object of the rmiInterface
     */
    public RMIConnection(RMIInterface clientRMIInterface) {
        rmiInterface = clientRMIInterface;
    }

    public RMIConnection() {

    }

    public void setRmiInterface(RMIInterface rmiInterface) {
        this.rmiInterface = rmiInterface;
    }

    /**
     * Send a maessage to the rmiInterface
     * @param message message to send
     * @return if the message has been correctly sent
     */
    @Override
    public boolean sendMessage(Message message) throws DisconnectedException {
        if (isConnected())
            try {
                LOGGER.log(Level.FINE,"Invio messaggio");
                rmiInterface.remoteSend(message);
                LOGGER.log(Level.FINE,"Messaggio inviato");
                return true;
            }catch (RemoteException e){
                LOGGER.log(Level.INFO,"Remote exception invio messaggio rmi");
                close();
                throw new DisconnectedException();
            }
        return false;
    }

    /**
     * Return the status of the connection
     * @return if the connection is still active
     */
    @Override
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Close the connection
     */
    @Override
    public void close() {
        this.deleteObservers();
        isConnected = false;
    }

    @Override
    public void remoteSend(Message message) throws RemoteException {
        LOGGER.log(Level.FINE,"Remote message ricevuto");
        setChanged();
        new Thread() {
            public void run() {
                notifyObservers(message);
            }}.start();

    }


    public void update(Observable o, Object arg) {
        LOGGER.log(Level.FINE,"update messaggio dalla view");
        try {
            sendMessage((Message) arg);
        } catch (DisconnectedException e) {
            //
        }
    }
}
