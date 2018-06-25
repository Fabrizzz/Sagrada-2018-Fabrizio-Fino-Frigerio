package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.exceptions.DisconnectedException;
import it.polimi.se2018.utils.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Socket connection
 * @author Alessio
 */
public class SocketConnection extends Connection implements Runnable {
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean connected = true;

    /**
     * Costructor
     * @param socket connection socket
     */
    public SocketConnection(Socket socket) {
        LOGGER.log(Level.FINE,"Creazione connessione");
        this.socket = socket;
        try {
            out = new ObjectOutputStream(this.socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(this.socket.getInputStream());
        }catch(IOException e){
            LOGGER.log(Level.SEVERE,"Errore creazione object stream");
            close();
        }
    }

    /**
     * Send message
     * @param message message to send
     */
    public boolean sendMessage(Message message) throws DisconnectedException {
        LOGGER.log(Level.FINE,"Invio messaggio");
        if (isConnected())
            try{
                out.writeObject(message);
                out.flush();
                out.reset();
                LOGGER.log(Level.FINE,"Messaggio inviato");
                return true;
            }catch (IOException e){
                close();
                throw new DisconnectedException();
            }
        return false;
    }


    /**
     * Close the connection
     */
    public synchronized void close() {
        LOGGER.log(Level.FINE,"Chiusura connessione");
        this.deleteObservers();

        try{
            out.close();
        }catch (IOException e){}

        try {
            in.close();
        } catch (IOException e){}

        try{
            socket.close();
        }catch (IOException e){}

        this.connected = false;

    }

    /**
     * Return the status of the connection
     * @return true if the client is connected, false otherwise
     */
    public synchronized boolean isConnected() {
        return connected;
    }

    @Override
    public void run() {
        Message message;
        while (connected) {
            try{
                message = (Message) in.readObject();
                LOGGER.log(Level.FINE,"Messaggio ricevuto");
                notifyObs(message);
            }catch (IOException | ClassNotFoundException e){
                LOGGER.log(Level.INFO,"Errore ricezione oggetto");
                close();
            }

        }
        close();
    }

    private void notifyObs(Message message){
        setChanged();
        new Thread() {
            public void run() {
                notifyObservers(message);
            }}.start();
    }
    @Override
    public void update(Observable o, Object arg) {
        try {
            sendMessage((Message) arg);
        } catch (DisconnectedException e) {
            //
        }
    }
}