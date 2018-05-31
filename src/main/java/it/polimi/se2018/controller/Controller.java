package it.polimi.se2018.controller;

import it.polimi.se2018.controller.chainOfResponsibilities.Handler;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.enums.BoardName;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;

import java.util.*;

public class Controller implements Observer {


    private boolean partitaIniziata = false;
    private Model model;
    private LinkedList<BoardName> availableBoards = new LinkedList<>();
    private Handler firstHandler;
    private Timer timer = new Timer();

    public Controller() {
        availableBoards.addAll(Arrays.asList(BoardName.values()));
        Collections.shuffle(availableBoards);
    }


    public List<BoardName> getBoards() {
        LinkedList<BoardName> boards = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            boards.add(availableBoards.getFirst());
            boards.addLast(boards.getLast().getCoppia());
            availableBoards.removeAll(boards);
        }
        return boards;
    }

    private Model getModel() {
        return model;
    }

    public synchronized void timerScaduto(int turn, int round) {
        if (model.getTurn() == turn && model.getRound() == round) {
            model.nextTurn();
            setTimer(model.getTurn(), model.getRound());
        }
    }

    public void setTimer(int turn, int round) {
        timer.schedule(new RoundTimer(turn, round, this), Model.getMinutesPerTurn() * 60 * 1000);
    }

    //private void startGame()


    @Override
    public synchronized void update(Observable o, Object arg) {
        PlayerMove playerMove = (PlayerMove) arg;
        RemoteView remoteView = (RemoteView) o;
        try {
            int turn = model.getTurn();
            firstHandler.process(playerMove, remoteView, getModel());
            if (model.getTurn() != turn)
                setTimer(model.getTurn(), model.getRound());
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }
}
