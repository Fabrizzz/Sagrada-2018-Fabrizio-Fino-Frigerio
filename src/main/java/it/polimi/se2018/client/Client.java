package it.polimi.se2018.client;

import it.polimi.se2018.view.CLI;
import it.polimi.se2018.view.GUINetwork;
import it.polimi.se2018.view.View;
import org.apache.commons.cli.*;

import java.io.*;
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
    public Client(String[] args){

        LOGGER.setLevel(Level.FINEST);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING   );
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

        CommandLine cmd;
        try{
            Options options = new Options();
            options.addOption("g", false, "Usa GUI");
            options.addOption("c", false, "Usa CLI");

            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse( options, args);
            if(cmd.hasOption("g")) {
                clientGUI();
                return;
            }else if(cmd.hasOption("c")) {
                clientCLI();
                return;
            }
        }catch (Exception e){
            LOGGER.log(Level.FINE,"Errore parser options");
        }

        int i;
        Scanner input = new Scanner(System.in);
        System.out.println("Scegli l'interfaccia grafica:");
        System.out.println("1) CLI");
        System.out.println("2) GUI");
        do {
            i = input.nextInt();
        } while (i < 1 || i > 2);

        switch (i) {
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
    private void clientCLI(){
        view = new CLI();
        clientNetwork = new ClientNetwork(view);
        ((CLI) view).createConnection(clientNetwork);
    }

    /**
     * Initialize GUI
     */
    private void clientGUI() {
        GUINetwork dialog = new GUINetwork();
        dialog.pack();
        dialog.setVisible(true);
        /*view = new GUIProxy();
        clientNetwork = new ClientNetwork(view);
        ((GUIProxy) view).setClientNetwork(clientNetwork);*/
    }
}
