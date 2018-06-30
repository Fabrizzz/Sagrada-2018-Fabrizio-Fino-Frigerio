package it.polimi.se2018.controller;

import java.util.TimerTask;

/**
 * It notify to the controller when the Timer expires every turn
 * @author Giampietro
 */
public class RoundTimer extends TimerTask {

    private int round;
    private int turn;
    private Controller controller;

    /**
     * Constructor
     * @param turn game turn
     * @param round game round
     * @param controller game controller
     */
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
