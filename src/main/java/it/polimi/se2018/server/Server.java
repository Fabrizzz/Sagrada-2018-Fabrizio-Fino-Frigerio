package it.polimi.se2018.server;

/**
 * Server main class
 * @author Alessio
 */
public class Server {
    //appena un client si connette, crea l' oggetto remoteview e gli associa la connection, così che poi la remoteview possa ricevere il mex iniziale per creare l oggetto player e registrarsi
    //col metodo registerConnection, il quale o chiamerà il metodo del controller per ricevere le 4 board e le manderà al player, oppure inserirà il player nella partita in corso, cambiando il
    //riferimento alla connection della remoteview esistente e rimettendo il boolean isConnected su true
    private ServerNetwork serverNetwork;

    /**
     * Costructor
     */
    public Server(){
        System.out.println("Avvio il server");
        serverNetwork = new ServerNetwork();
    }
}
