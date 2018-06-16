package it.polimi.se2018.server;

import it.polimi.se2018.utils.InputUtils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.*;

/**
 * Server main class
 * @author Alessio
 */
public class Server {
    private static final Logger LOGGER = Logger.getLogger("Logger");

    //appena un client si connette, crea l' oggetto remoteview e gli associa la connection, così che poi la remoteview possa ricevere il mex iniziale per creare l oggetto player e registrarsi
    //col metodo registerConnection, il quale o chiamerà il metodo del controller per ricevere le 4 board e le manderà al player, oppure inserirà il player nella partita in corso, cambiando il
    //riferimento alla connection della remoteview esistente e rimettendo il boolean isConnected su true
    /**
     * Costructor
     */
    public Server(){
        LOGGER.setLevel(Level.FINEST);

        FileHandler fh;
        try {
            fh = new FileHandler("sagradaServer.log");
            fh.setLevel(Level.FINEST);
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.SEVERE);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);
        int port = 0;

        do {
            System.out.println("Inserire la porta: ");
            port = InputUtils.getInt();
        }while(!available(port));

        new ServerNetwork().start(port);

    }

    public static boolean available(int port) {
        if (port < 1000 || port > 65535) {
            System.out.println("Porta non in range(1000-65535)");
            return false;
        }

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }
        System.out.println("Porta occupata");
        return false;
    }
}
