package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.Message;

import java.util.Observable;

public class RemoteView extends Observable {

    private Player player;
    //private Connection connection;

    public Player getPlayer() {
        return player;
    }

    public void sendBack(Message message) {
        // da scrivere
    }
}
