package it.polimi.se2018.server;

import java.io.IOException;
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

        System.out.println("Inserire porta: ");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        new ServerNetwork().start(port);
    }
}
