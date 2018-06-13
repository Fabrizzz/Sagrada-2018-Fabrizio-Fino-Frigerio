package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

/**
 * Socket connection
 * @author Alessio
 */
public class SocketConnection extends Connection implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean connected = true;

    /**
     * Costructor
     * @param socket connection socket
     */
    public SocketConnection(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(this.socket.getOutputStream());
            in = new ObjectInputStream(this.socket.getInputStream());
        }catch(IOException e){
            e.printStackTrace();
            close();
        }
    }

    /**
     * Send message
     * @param message message to send
     */
    public boolean sendMessage(Message message){
        if (isConnected())
            try{
                out.writeObject(message);
                out.flush();
                return true;
            }catch (IOException e){
                close();
                return false;
            }
        return false;
    }


    /**
     * Close the connection
     */
    public synchronized void close() {
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
                System.out.println("Messaggio ricevuto");
                setChanged();
                notifyObservers(message);

            }catch (IOException | ClassNotFoundException e){
                close();
            }

        }
        close();
    }

    @Override
    public void update(Observable o, Object arg) {
        sendMessage((Message) arg);
    }
}