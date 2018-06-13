package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.messages.Message;

import java.rmi.RemoteException;
import java.util.Observable;

/**
 * Class rapresenting the rmi connection from the server to the rmiInterface
 * @author Alessio
 */
public class RMIConnection extends Connection implements RMIInterface {

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
    public boolean sendMessage(Message message) {
        if (isConnected())
            try {
                rmiInterface.remoteSend(message);
                return true;
            }catch (RemoteException e){
                close();
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
        setChanged();
        notifyObservers(message);
    }


    public void update(Observable o, Object arg) {
        sendMessage((Message) arg);
    }
}
