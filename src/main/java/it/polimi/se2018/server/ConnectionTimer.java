package it.polimi.se2018.server;

import java.util.TimerTask;

public class ConnectionTimer extends TimerTask {

    private int game;
    private ServerNetwork serverNetwork;

    public ConnectionTimer(ServerNetwork serverNetwork, int game) {
        this.game = game;
        this.serverNetwork = serverNetwork;
    }

    @Override
    public void run() {
        serverNetwork.timerScaduto(game);
    }
}
