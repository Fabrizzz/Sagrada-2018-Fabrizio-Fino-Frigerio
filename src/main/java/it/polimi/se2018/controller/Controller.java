package it.polimi.se2018.controller;

import it.polimi.se2018.controller.chainOfResponsibilities.Handler;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.BoardList;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.messages.PlayerMove;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Observer {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    private boolean partitaIniziata = false;
    private Model model;
    private Handler firstHandler;
    private RemoteView views;
    private Timer timer = new Timer();
    private BoardList boardList;

    public Controller() {
        boardList = new BoardList();
    }


    public List<Board> getBoards() {
        LinkedList<Board> boards = new LinkedList<>();
        Board[] coppia = new Board[2];
        coppia = boardList.getCouple();
        boards.add(coppia[0]);
        boards.add(coppia[1]);
        coppia = boardList.getCouple();
        boards.add(coppia[0]);
        boards.add(coppia[1]);
        return boards;
    }

    public void startGame(Collection<RemoteView> views) {
        LOGGER.log(Level.INFO,"Avvio gioco");
        /*if (views.size() < 2 || views.size() > 4)
            throw new IllegalArgumentException();
        List publicObjectiveNames = Arrays.asList(PublicObjectiveName.values());
        Collections.shuffle(publicObjectiveNames);
        List<PublicObjective> publicObjectives = new ArrayList<>();
        publicObjectiveNames.subList(0, 3).forEach(k -> publicObjectives.add(PublicObjectiveFactory.createPublicObjective((PublicObjectiveName) k)));

        List colors = Arrays.asList(Color.values());
        Collections.shuffle(colors);
        Iterator<Color> iterator = colors.iterator();
        Map<Player, PrivateObjective> privateObjectiveMap = boards.keySet().stream().collect(Collectors.toMap(k -> k, t -> new PrivateObjective(iterator.next())));

        this.model = new Model(new ArrayList<>(boards.keySet()), publicObjectives, boards, privateObjectiveMap, Tool.getRandTools(3));
        setTimer(0, 0);*/
    }

    private Model getModel() {
        return model;
    }

    public synchronized void timerScaduto(int turn, int round) {
        LOGGER.log(Level.FINE,"Timer scaduto");
        if (model.getTurn() == turn && model.getRound() == round) {
            model.nextTurn();
            setTimer(model.getTurn(), model.getRound());
        }
    }

    public void setTimer(int turn, int round) {
        LOGGER.log(Level.FINE,"Timer impostato");
        timer.schedule(new RoundTimer(turn, round, this), (long) (Model.getMinutesPerTurn() * 60 * 1000));
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        LOGGER.log(Level.INFO,"PlayerMove ricevuta");
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
