package it.polimi.se2018.utils.network;

import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.network.Connection;
import it.polimi.se2018.utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Connessione socket con il client
 * @author Alessio
 */
public class SocketConnection extends Thread implements Connection {
    private Socket socket;
    private ServerNetwork serverNetwork;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Costruttore
     * @param serverNetwork server
     * @param socket socket della connessione istaurata con il client
     */
    public SocketConnection(ServerNetwork serverNetwork, Socket socket) {
        this.serverNetwork = serverNetwork;
        this.socket = socket;
        try {
            out = new ObjectOutputStream(this.socket.getOutputStream());
            in = new ObjectInputStream(this.socket.getInputStream());
        }catch(IOException e){
            close();
        }
    }

    /**
     * Invia messaggio al client
     * @param message messaggio da inviare
     */
    public void sendMessage(Message message){
        try{
            out.writeObject(message);
            out.flush();
        }catch (IOException e){
            close();
        }
    }

    /**
     * Chiude connessione
     */
    public void close(){
        try{
            out.close();
        }catch (IOException e){}

        try {
            in.close();
        } catch (IOException e){}

        try{
            socket.close();
        }catch (IOException e){}

        serverNetwork.removeClient(this);
    }

    @Override
    public void run() {
        Message message;
        while (true) {
            try{
                message = (Message) in.readObject();
                serverNetwork.reciveMessage(message,this);
            }catch (IOException | ClassNotFoundException e){
                break;
            }

        }
        close();
    }
}