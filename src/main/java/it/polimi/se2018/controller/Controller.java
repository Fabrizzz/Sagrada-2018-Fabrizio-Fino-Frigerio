package it.polimi.se2018.controller;

import it.polimi.se2018.controller.chainOfResponsibilities.Handler;
import it.polimi.se2018.controller.chainOfResponsibilities.ToolFactory;
import it.polimi.se2018.model.*;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.MessageType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Controller implements Observer {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    private Model model;
    private Handler firstHandler;
    BoardList boardList = new BoardList();
    private List<RemoteView> views;
    private Map<Player, PlayerBoard> choosenBoards = new HashMap();
    private Timer timer = new Timer();
    //private ModelView modelView;

    public Controller() {}

    public Board[] getBoards() {
        LinkedList<Board> boards = new LinkedList<>();
        Board[] coppia;
        coppia = boardList.getCouple();
        boards.add(coppia[0]);
        boards.add(coppia[1]);
        coppia = boardList.getCouple();
        boards.add(coppia[0]);
        boards.add(coppia[1]);
        Board[] board = new Board[4];

        return boards.toArray(board);
    }

    public synchronized void startGame(Collection<RemoteView> views) {
        if (views.size() < 2 || views.size() > 4)
            throw new IllegalArgumentException();

        this.views = new ArrayList<>(views);
   /*     for(int i = 0; i < this.views.size(); i++){
            this.views.get(i).addObserver(this);
        }

        for(int i = 0; i < this.views.size(); i++){
            LOGGER.log(Level.FINE,"Invio messaggio board");
            this.views.get(i).sendBack(new ServerMessage(getBoards()));
        }*/

        this.views.stream().forEach(k -> k.addObserver(this));
        this.views.stream().forEach(k -> k.sendBack(new ServerMessage(getBoards())));
        try {
            LOGGER.log(Level.INFO,"Attendo board");
            while (choosenBoards.size() < views.size())
                wait();
            LOGGER.log(Level.INFO,"Avvio gioco");
            List publicObjectiveNames = Arrays.asList(PublicObjectiveName.values());
            Collections.shuffle(publicObjectiveNames);
            List<PublicObjective> publicObjectives = new ArrayList<>();
            publicObjectiveNames.subList(0, 3).forEach(k -> publicObjectives.add(PublicObjectiveFactory.createPublicObjective((PublicObjectiveName) k)));
            List colors = Arrays.asList(Color.values());
            Collections.shuffle(colors);
            Iterator<Color> iterator = colors.iterator();
            Map<Player, PrivateObjective> privateObjectiveMap = views.stream().collect(Collectors.toMap(k -> k.getPlayer(), t -> new PrivateObjective(iterator.next())));
            List<Tool> tools = Tool.getRandTools(3);

            this.model = new Model(new ArrayList<>(choosenBoards.keySet()), publicObjectives, choosenBoards, privateObjectiveMap, tools,this);
            //setta gli handler, setta i vari observer, invia a tutti la modelview
            firstHandler = ToolFactory.createFirstHandler();
            Handler temp = firstHandler;
            for (Tool tool : tools) {
                temp = temp.setNextHandler(ToolFactory.createToolHandler(tool));
            }
            temp = temp.setNextHandler(ToolFactory.createToolHandler(Tool.MOSSASTANDARD));
            temp.setNextHandler(ToolFactory.createLastHandler());
            
            for (RemoteView view : views) {
                model.addObserver(view);
                view.sendBack(new ServerMessage(MessageType.INITIALCONFIGSERVER, new ModelView(model)));
            }

            setTimer(0, 0);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE,"Interrupt ricevuto");
            Thread.currentThread().interrupt();
        }

    }

    public Model getModel() {
        return model;
    }

    private synchronized void timerScaduto(int turn, int round) {
        LOGGER.log(Level.FINE,"Timer scaduto");

        if (model.getTurn() == turn && model.getRound() == round) {
            nextTurn();
            setTimer(model.getTurn(), model.getRound());
        }

    }

    private void setTimer(int turn, int round) {
        LOGGER.log(Level.FINE,"Timer impostato");
        timer.schedule(new RoundTimer(turn, round, this), (long) (Model.getMinutesPerTurn() * 60 * 1000));
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        ClientMessage message = (ClientMessage) arg;
        RemoteView remoteView = (RemoteView) o;

        if (message.getMessageType() == MessageType.PLAYERMOVE) {

            PlayerMove playerMove = message.getPlayerMove();
            LOGGER.log(Level.INFO, "PlayerMove ricevuta");

            try {
                int turn = model.getTurn();
                firstHandler.process(playerMove, remoteView, getModel());
                if (model.getTurn() != turn)
                    setTimer(model.getTurn(), model.getRound());
            } catch (InvalidParameterException e) {
                LOGGER.log(Level.SEVERE,"Parametri invalidi");
            }
        } else if (message.getMessageType() == MessageType.CHOSENBOARD) {
            LOGGER.log(Level.INFO, "Board ricevuta");
            choosenBoards.put(remoteView.getPlayer(), new PlayerBoard(message.getBoardName()));
            notifyAll();
        }
    }

    /**
     * Change the current player turn
     */
    public void nextTurn() { //da spostare nel controller
        //aggiungere che se rimane un solo giocatore, ha vinto
        LOGGER.log(Level.INFO,"Next turn chiamanto");
        model.setUsedTool(false);
        model.setNormalMove(false);
        int playerPosition = (model.getTurn() < model.getPlayers().size()) ? model.getTurn() : model.getPlayers().size() * 2 -  model.getTurn() - 1;
        model.getPlayers().get(playerPosition).setYourTurn(false);
        model.setTurn(model.getTurn() + 1);

        if (model.getTurn() == model.getPlayers().size() * 2)
            model.endRound();
        else if (model.getTurn() == model.getPlayers().size())
            model.setFirstTurn(false);


        if (model.getRound() != 10) {

            playerPosition = (model.getTurn() < model.getPlayers().size()) ? model.getTurn() : model.getPlayers().size() * 2 - model.getTurn() - 1;
            if (model.getPlayers().get(playerPosition).isSkipSecondTurn()) {
                LOGGER.log(Level.FINE,"Is skip second turn");
                model.getPlayers().get(playerPosition).setSkipSecondTurn(false);
                nextTurn();
            } else {
                LOGGER.log(Level.FINE,"Is NOT skip second turn");
                model.getPlayers().get(playerPosition).setYourTurn(true);
                if (!model.getPlayers().get(playerPosition).isConnected()){
                    LOGGER.log(Level.FINE,"Il prossimo giocatore e' disconnesso");
                    nextTurn();
                } else {
                    //timer.schedule(new RoundTimer(getTurn(), getRound(), this), MINUTES_PER_TURN * 60 * 1000);
                    LOGGER.log(Level.FINE,"Prossimo giocatore: " + model.getPlayers().get(playerPosition).getNick() +  ". Notifico gli osservatori");
                    model.notifyObs();
                }
            }
        } else
            model.endGame();
    }
}
