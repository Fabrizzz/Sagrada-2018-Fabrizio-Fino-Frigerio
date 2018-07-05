package it.polimi.se2018.view;

import java.util.Observable;

public class GUISwingProxy extends View {
    private GUISwing gameWindow;

    public void gameWindow(GUISwing gameWindow){
        this.gameWindow = gameWindow;
    }
    @Override
    public void connectionClosed() {

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
