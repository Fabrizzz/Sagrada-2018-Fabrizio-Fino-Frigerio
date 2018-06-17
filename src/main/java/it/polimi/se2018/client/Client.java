package it.polimi.se2018.client;

import it.polimi.se2018.View.CLI;
import it.polimi.se2018.View.GUIProxy;
import it.polimi.se2018.View.View;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.*;

/**
 * Client main class
 * @author Alessio
 */
public class Client {
    private View view;
    private ClientNetwork clientNetwork;
    private static final Logger LOGGER = Logger.getLogger("Logger");

    /**
     * Constructor
     */
    public Client(){
        LOGGER.setLevel(Level.FINEST);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        FileHandler fh;
        try {
            fh = new FileHandler("sagradaClient.log");
            fh.setLevel(Level.FINEST);
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            LOGGER.log(Level.WARNING,"Security error exception");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Impossibile aprire file di log");
        }

        int i;
        Scanner input = new Scanner(System.in);
        System.out.println("Scegli l'interfaccia grafica:");
        System.out.println("1) CLI");
        System.out.println("2) GUI");
        do{
            i = input.nextInt();
        }while(i < 1 || i > 2);

        switch (i){
            case 1:
                clientCLI();
                break;
            case 2:
                clientGUI();
                break;
        }
    }

    /**
     * Initialize the command line interface
     */
    public void clientCLI(){
        view = new CLI();
        clientNetwork = new ClientNetwork(view);
        ((CLI) view).createConnection(clientNetwork);
    }

    /**
     * Initialize GUI
     */
    private void clientGUI() {
        view = new GUIProxy();
        clientNetwork = new ClientNetwork(view);
        ((GUIProxy) view).setClientNetwork(clientNetwork);
    }
}
