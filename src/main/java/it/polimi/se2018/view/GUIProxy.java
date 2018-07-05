package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;

import java.util.Observable;

/**
 * @author Matteo
 */
public class GUIProxy extends View {


    private ClientNetwork clientNetwork;
    private GUI startUpTest;


    public GUIProxy(){
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
        startUpTest.sendInfo(clientNetwork);
    }

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

}
