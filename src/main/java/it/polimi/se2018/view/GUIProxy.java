package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.utils.messages.ServerMessage;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Observable;

/**
 * @author Matteo
 */
public class GUIProxy extends View {


    private ClientNetwork clientNetwork;
    private GUI startUpTest;
    private ServerMessage message;
    private IntegerProperty num;
    private int i;


    public GUIProxy(){
        num = new SimpleIntegerProperty(1);
        launchGUI();

    }

    private void launchGUI() {

        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(GUI.class);
            }
        }.start();
        startUpTest = GUI.waitStartUpGUI();
        startUpTest.messageStartUpGUI();
    }

    public void setClientNetwork(ClientNetwork temp) {
        clientNetwork = temp;
        startUpTest.sendInfo(clientNetwork, message, num);
    }

    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        message = (ServerMessage) arg;
        i = num.getValue();
        i++;
        num.setValue(i);
    }

    @Override
    public void connectionClosed() {
        System.out.println("Connessione al server chiusa");

    }

}
