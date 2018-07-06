package it.polimi.se2018.controller;

import it.polimi.se2018.controller.chainOfResponsibilities.Handler;
import it.polimi.se2018.controller.chainOfResponsibilities.ToolFactory;
import it.polimi.se2018.model.*;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.utils.JSONUtils;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.ErrorType;
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

/**
 * This class control the game logic
 * @author Giampietro
 */

public class Controller implements Observer {

    private static final Logger LOGGER = Logger.getLogger("Logger");
    private Model model;
    private Handler firstHandler;
    private BoardList boardList = new BoardList();
    private List<RemoteView> views;
    private Map<Player, PlayerBoard> choosenBoards = new HashMap();
    private Timer timer = new Timer();
    private int roundTimer = 1;
    private boolean gameEnded = false;


    /**
     * It return two playerboard couples to choose
     * @return array of lenght = 4
     */
    public Board[] getBoards() {
        LinkedList<Board> boards = new LinkedList<>();
        try {
            for (int i = 0; i < 2; i++)
                boards.addAll(Arrays.asList(boardList.getCouple()));
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }

        Board[] board = new Board[4];

        return boards.toArray(board);
    }

    /**
     * it starts a new game by asking players to choose a board and initializing the Model and the tool handlers
     * @param views remoteviews
     */
    public synchronized void startGame(Collection<RemoteView> views) {
        roundTimer = JSONUtils.readRoundTimer();

        if (views.size() < 2 || views.size() > 4)
            throw new IllegalArgumentException();

        this.views = new ArrayList<>(views);

        this.views.stream().forEach(k -> k.addObserver(this));
        this.views.stream().forEach(k -> k.sendBack(new ServerMessage(getBoards())));
        try {
            LOGGER.log(Level.INFO, "In attesa di ricevere Board dai client");
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
            /*ArrayList<Tool> tools = new ArrayList<>();
            tools.add(Tool.LATHEKIN);
            tools.add(Tool.TAGLIERINACIRCOLARE);
            tools.add(Tool.PENNELLOPERPASTASALDA);*/
            ArrayList<Player> players = new ArrayList<>(views.stream().map(k -> k.getPlayer()).collect(Collectors.toList()));

            for (int j = 0; j < tools.size(); j++){
                LOGGER.log(Level.FINE,"Tool 1 = " + tools.get(j).toString());
            }

            for(Player player : choosenBoards.keySet()) {
                player.setFavorTokens(choosenBoards.get(player).getBoardDifficulty());
            }

            this.model = new Model(players, publicObjectives, choosenBoards, privateObjectiveMap, tools);
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
                view.sendBack(new ServerMessage(MessageType.INITIALCONFIGSERVER, new ModelView(model,model.getPrivateObjective(view.getPlayer()))));
            }

            setTimer(0, 0);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Interrupt ricevuto durante l' attesa delle boards");
            Thread.currentThread().interrupt();
        }

    }

    /**
     * Return the game model
     * @return the game model
     */
    public Model getModel() {
        return model;
    }

    /**
     * it calls nextTurn() when the timer expires, if the turn hasn't ended yet
     * @param turn turn when the timer was called
     * @param round round when the timer was called
     */
    public synchronized void timerScaduto(int turn, int round) {

        if (model.getTurn() == turn && model.getRound() == round && !gameEnded) {
            LOGGER.log(Level.INFO, "Timer scaduto, turno interrotto e nextTurn chiamata");
            System.out.println("Timer scaduto");
            nextTurn();
            setTimer(model.getTurn(), model.getRound());
            model.notifyObs();
        }

    }

    /**
     * It sets the Timer
     * @param turn turn when the timer was called
     * @param round round when the timer was called
     */
    private void setTimer(int turn, int round) {
        LOGGER.log(Level.FINER, "Timer impostato");
        timer.schedule(new RoundTimer(turn, round, this), (long) (roundTimer * 60 * 1000));
    }

    /**
     * End the game and send final scores to every player
     */
    public synchronized void endGame() {
        gameEnded = true;
        //chiudere i timer
        //cancellare observers, chiudere connection, deregistrare connections dal server

        LOGGER.log(Level.FINE,"Endgame");
    }

    /**
     * End the round and update the round variables
     */
    private void endRound() {
        getModel().setRound(getModel().getRound() + 1);
        if (getModel().getRound() == 10)
            endGame();
        else {
            getModel().setTurn(0);
            getModel().setFirstTurn(true);
            getModel().getRoundTrack().addDice(getModel().getRound(), getModel().getDraftPool().removeAll());
            Collections.rotate(getModel().getPlayers(), -1);
            getModel().getDraftPool().rollDice(getModel().getDiceBag());
        }

    }

    /**
     * Check if there are more than one player still playing
     * @return true if the count of connected players is > 1 false otherwise
     */
    public boolean playerCountCheck(){
        return true;
        /*int p = 0;

        for(RemoteView remoteView : views){
            if(remoteView.isConnected()){
                p ++;
            }
        }
        if(p > 1){
            return true;
        }else{
            return false;
        }*/
    }
    /**
     * Change the current player turn
     */
    public synchronized void nextTurn() {

        LOGGER.log(Level.FINE, "Next turn chiamato");
        model.setUsedTool(false);
        model.setNormalMove(false);
        int playerPosition = (model.getTurn() < model.getPlayers().size()) ? model.getTurn() : model.getPlayers().size() * 2 - model.getTurn() - 1;
        model.getPlayers().get(playerPosition).setYourTurn(false);
        model.setTurn(model.getTurn() + 1);

        if (model.getTurn() == model.getPlayers().size() * 2)
            endRound();
        else if (model.getTurn() == model.getPlayers().size())
            model.setFirstTurn(false);


        if (model.getRound() != 10) {

            int playerPosition2 = (model.getTurn() < model.getPlayers().size()) ? model.getTurn() : model.getPlayers().size() * 2 - model.getTurn() - 1;
            model.getPlayers().get(playerPosition2).setYourTurn(true);
            if (model.getPlayers().get(playerPosition2).isSkipSecondTurn()) {
                LOGGER.log(Level.FINE, "Il giocatore " + model.getPlayers().get(playerPosition2).getNick() + " salta il secondo turno a causa di una tool");
                model.getPlayers().get(playerPosition2).setSkipSecondTurn(false);
                nextTurn();
            } else if (!views.stream().filter(k -> k.getPlayer() == model.getPlayers().get(playerPosition2)).findAny().orElseThrow(RuntimeException::new).isConnected()) {
                LOGGER.log(Level.FINE, "Il giocatore " + model.getPlayers().get(playerPosition2).getNick() + " passa il turno poichè disconnesso");
                nextTurn();
            } else {
                LOGGER.log(Level.FINE, "Prossimo giocatore: " + model.getPlayers().get(playerPosition2).getNick() + ". Notifico gli osservatori");
            }
        }

    }


    /**
     * Gestisce i messaggi ricevuti dalla remoteview
     * @param o RemoteView
     * @param arg ClientMessage
     */
    @Override
    public synchronized void update(Observable o, Object arg) {
        if(!playerCountCheck()){
            LOGGER.log(Level.FINE,"1 giocatore rimanenti, termino partita");
            endGame();
        } else {
            try {
                ClientMessage message = (ClientMessage) arg;
                RemoteView remoteView = (RemoteView) o;


                if (message.getMessageType() == MessageType.PLAYERMOVE) {

                    PlayerMove playerMove = message.getPlayerMove();
                    LOGGER.log(Level.INFO, "PlayerMove ricevuta");

                    try {
                        if (firstHandler.process(playerMove, remoteView, getModel())) {

                            if ((playerMove.getTool().equals(Tool.SKIPTURN)) || model.hasUsedTool() && model.hasUsedNormalMove()) {
                                nextTurn();
                                setTimer(model.getTurn(), model.getRound());
                                LOGGER.log(Level.FINE, "Mossa ricevuta e turno finito");
                            }
                            model.notifyObs();
                        }

                    } catch (InvalidParameterException e) {
                        LOGGER.log(Level.SEVERE, "Ricevuta PlayerMove con parametri invalidi");
                        remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                    }

                } else if (message.getMessageType() == MessageType.CHOSENBOARD) {
                    LOGGER.log(Level.INFO, "Board ricevuta");
                    choosenBoards.put(remoteView.getPlayer(), new PlayerBoard(message.getBoardName()));
                    notifyAll();
                } else if (message.getMessageType() == MessageType.HASDISCONNECTED) {
                    LOGGER.log(Level.INFO, "Notifico la disconnessione di " + remoteView.getPlayer().getNick());
                    for (RemoteView view : views) {
                        view.sendBack(new ServerMessage(MessageType.HASDISCONNECTED, remoteView.getPlayer().getNick()));
                    }
                    if (remoteView.getPlayer().isYourTurn()) {
                        LOGGER.log(Level.FINE, "Era il turno del giocatore disconnesso, passo al nuovo turno");
                        nextTurn();
                        setTimer(model.getTurn(), model.getRound());
                        getModel().notifyObs();
                    }
                } else if (message.getMessageType() == MessageType.HASRICONNECTED) {
                    LOGGER.log(Level.INFO, remoteView.getPlayer().getNick() + " si è riconnesso");
                    for (RemoteView view : views) {
                        view.sendBack(new ServerMessage(MessageType.HASRICONNECTED, remoteView.getPlayer().getNick()));
                    }
                    remoteView.sendBack(new ServerMessage(MessageType.INITIALCONFIGSERVER, new ModelView(model, model.getPrivateObjective(remoteView.getPlayer()))));
                }
            } catch (ClassCastException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
    }

}
