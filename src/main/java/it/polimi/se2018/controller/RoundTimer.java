package it.polimi.se2018.controller;

import it.polimi.se2018.model.Model;

import java.util.TimerTask;

public class RoundTimer extends TimerTask {

    private int round;
    private int turn;
    private Model model;

    public RoundTimer(int turn, int round, Model model) {
        this.round = round;
        this.turn = turn;
        this.model = model;
    }

    @Override
    public void run() {
        model.setTimerScadutoOn(turn, round);
    }
}
