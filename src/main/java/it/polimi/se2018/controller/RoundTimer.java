package it.polimi.se2018.controller;

import java.util.TimerTask;

public class RoundTimer extends TimerTask {

    private int round;
    private int turn;
    private Controller controller;

    public RoundTimer(int turn, int round, Controller controller) {
        this.round = round;
        this.turn = turn;
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.timerScaduto(turn, round);
    }
}
