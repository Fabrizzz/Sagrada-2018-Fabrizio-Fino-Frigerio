package it.polimi.se2018.View;

import it.polimi.se2018.client.ClientNetwork;

import java.util.Observable;

/**
 * @author Matteo
 */
public class GUIProxy extends View {

    /*
    Da fare; bisogna utilizzare questa classe come tramite tra la GUI e il client che la istanzia.
    In particolare deve fare da tramite per gli aspetti inziali di connessione e per i messaggi
    relativi alle mosse fatte dall'utente. Nella classe Client occorre come dette istanziare questa
    classe che farà da tramite.
     */

    /*public GUIProxy(){

    }*/


    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void connectionClosed() {

    }

    //Potrebbe servire
    public void createConnection(ClientNetwork clientNetwork) {
    }
}
