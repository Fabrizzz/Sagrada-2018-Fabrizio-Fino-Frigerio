package it.polimi.se2018.client;

import it.polimi.se2018.View.CLI;
import it.polimi.se2018.View.View;

public class Client {
    private View view;
    private ClientNetwork clientNetwork;

    public void clientCLI(){
        view = new CLI();
        clientNetwork = new ClientNetwork(view);
        ((CLI) view).createConnection(clientNetwork);
    }
}
