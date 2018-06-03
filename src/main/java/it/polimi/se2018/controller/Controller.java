package it.polimi.se2018.controller;

import it.polimi.se2018.controller.chainOfResponsibilities.Handler;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.enums.BoardName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;

import java.util.*;
import java.util.stream.Collectors;

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

    public void startGame(Map<Player, PlayerBoard> boards) {
        List publicObjectiveNames = Arrays.asList(PublicObjectiveName.values());
        Collections.shuffle(publicObjectiveNames);
        List<PublicObjective> publicObjectives = new ArrayList<>();
        publicObjectiveNames.subList(0, 3).forEach(k -> publicObjectives.add(PublicObjectiveFactory.createPublicObjective((PublicObjectiveName) k)));

        List colors = Arrays.asList(Color.values());
        Collections.shuffle(colors);
        Iterator<Color> iterator = colors.iterator();
        Map<Player, PrivateObjective> privateObjectiveMap = boards.keySet().stream().collect(Collectors.toMap(k -> k, t -> new PrivateObjective(iterator.next())));

        this.model = new Model(new ArrayList<>(boards.keySet()), publicObjectives, boards, privateObjectiveMap, Tool.getRandTools(3));
        setTimer(0, 0);
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
