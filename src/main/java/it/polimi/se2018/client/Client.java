package it.polimi.se2018.client;

import it.polimi.se2018.View.CLI;
import it.polimi.se2018.View.View;

/**
 * Client main class
 * @author Alessio
 */
public class Client {
    private View view;
    private ClientNetwork clientNetwork;

    public Client(){
        System.out.println("Scegli l'interfaccia grafica");
        System.out.println("1) CLI");
        clientCLI();

    }

    /**
     * Initialize the command line interface
     */
    public void clientCLI(){
        view = new CLI();
        clientNetwork = new ClientNetwork(view);
        ((CLI) view).createConnection(clientNetwork);
    }
}
