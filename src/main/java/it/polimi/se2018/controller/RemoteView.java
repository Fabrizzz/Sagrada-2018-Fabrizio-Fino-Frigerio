package it.polimi.se2018.controller;

import it.polimi.se2018.View.View;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Message;

import java.util.Observable;

public class RemoteView extends View {

    private Player player;

    public RemoteView(Player player) {
        this.player = player;
    }

    //private Connection connection;

    public Player getPlayer() {
        return player;
    }

    public void sendBack(Message message) {
        // da scrivere
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void connectionClosed() {

    }
}
