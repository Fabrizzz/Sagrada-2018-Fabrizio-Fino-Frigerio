package it.polimi.se2018.utils.network;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.enums.MessageType;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.Message;

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
    private NetworkHandler networkHandler;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean connected = true;

    /**
     * Costructor
     * @param networkHandler server connection manager
     * @param socket connection socket
     */
    public SocketConnection(NetworkHandler networkHandler, Socket socket) {
        this.networkHandler = networkHandler;
        this.socket = socket;
        try {
            out = new ObjectOutputStream(this.socket.getOutputStream());
            in = new ObjectInputStream(this.socket.getInputStream());
        }catch(IOException e){
            close();
        }
    }

    /**
     * Send message
     * @param message message to send
     */
    public boolean sendMessage(Message message){
        try{
            out.writeObject(message);
            out.flush();
            return true;
        }catch (IOException e){
            close();
            return false;
        }
    }


    /**
     * Close the connection
     */
    public void close(){
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
        networkHandler.closeConnection(this);

    }

    /**
     * Return the status of the connection
     * @return true if the client is connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void run() {
        Message message;
        while (connected) {
            try{
                message = (Message) in.readObject();
                if(message.getMessageType() == MessageType.INITIALCONFIG){
                    System.out.println("Messaggio INITIALCONFIG ricevuto");
                    RemoteView remoteView = ((ServerNetwork) this.networkHandler).initializeConnection(this,message);
                    if(remoteView != null){
                        addObserver(remoteView);
                    }else{
                        close();
                    }
                }else {
                    System.out.println("Messaggio ricevuto");
                    setChanged();
                    notifyObservers(message);
                }
            }catch (IOException | ClassNotFoundException e){
                connected = false;
            }

        }
        close();
    }

    @Override
    public void update(Observable o, Object arg) {
        sendMessage((Message) arg);
    }
}