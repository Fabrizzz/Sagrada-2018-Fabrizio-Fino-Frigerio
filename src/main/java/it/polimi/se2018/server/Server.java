package it.polimi.se2018.server;

import it.polimi.se2018.utils.InputUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.*;

/**
 * Server main class
 * @author Alessio
 */
public class Server {
    private static final Logger LOGGER = Logger.getLogger("Logger");

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
        return false;
    }
}
