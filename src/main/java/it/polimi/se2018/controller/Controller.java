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

    //private void startGame()


    @Override
    public synchronized void update(Observable o, Object arg) {
        PlayerMove playerMove = (PlayerMove) arg;
        RemoteView remoteView = (RemoteView) o;
        try {
            firstHandler.process(playerMove, remoteView, getModel());
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }
}
