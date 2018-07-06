package it.polimi.se2018.controller;

import it.polimi.se2018.controller.chainOfResponsibilities.Handler;
import it.polimi.se2018.controller.chainOfResponsibilities.ToolFactory;
import it.polimi.se2018.model.*;
import it.polimi.se2018.objective_cards.PrivateObjective;
import it.polimi.se2018.objective_cards.PublicObjective;
import it.polimi.se2018.objective_cards.PublicObjectiveName;
import it.polimi.se2018.objective_cards.public_cards.PublicObjectiveFactory;
import it.polimi.se2018.server.ServerNetwork;
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
    private ServerNetwork server;
    private Timer timer = new Timer();
    private int roundTimer = 1;
    private boolean gameEnded = false;


    public Controller(ServerNetwork serverNetwork) {
        server = serverNetwork;
    }

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
        this.views.stream().forEach(k -> k.elaborateMessage(new ServerMessage(getBoards())));
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
                view.elaborateMessage(new ServerMessage(MessageType.INITIALCONFIGSERVER, new ModelView(model, model.getPrivateObjective(view.getPlayer()))));
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

        if (!gameEnded && model.getTurn() == turn && model.getRound() == round) {
            LOGGER.log(Level.INFO, "Timer scaduto, turno interrotto e nextTurn chiamata");
            System.out.println("Timer scaduto");
            nextTurn();
            setTimer(model.getTurn(), model.getRound());
            if (!gameEnded)
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
    public synchronized void endGame(boolean onlyOnePlayer) {
        if (!gameEnded) {
            gameEnded = true;
            timer.cancel();
            Map<String, Integer> mappa = new HashMap<>();
            if (onlyOnePlayer) {
                RemoteView winner = views.stream().filter(k -> k.isConnected()).findAny().orElseThrow(RuntimeException::new);
                PlayerBoard playerBoard = model.getBoard(winner.getPlayer());
                int punteggio = 0;
                List<PublicObjective> publicObjectives = model.getPublicObjectives();
                punteggio += publicObjectives.stream().mapToInt(k -> k.getPoints(playerBoard)).sum();
                punteggio += model.getPrivateObjective(winner.getPlayer()).getPoints(playerBoard);
                punteggio += winner.getPlayer().getFavorTokens();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (!playerBoard.containsDie(i, j))
                            punteggio--;
                    }
                }
                mappa.put(winner.getPlayer().getNick(), punteggio);
                winner.elaborateMessage(new ServerMessage(mappa, winner.getPlayer().getNick()));
                winner.connectionClosed();
            } else {
                String vincitore = "";
                List<Player> winners = new ArrayList<>();
                int max = -1000;
                for (RemoteView view : views) {
                    PlayerBoard playerBoard = model.getBoard(view.getPlayer());
                    int punteggio = 0;
                    List<PublicObjective> publicObjectives = model.getPublicObjectives();
                    punteggio += publicObjectives.stream().mapToInt(k -> k.getPoints(playerBoard)).sum();
                    punteggio += model.getPrivateObjective(view.getPlayer()).getPoints(playerBoard);
                    punteggio += view.getPlayer().getFavorTokens();
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (!playerBoard.containsDie(i, j))
                                punteggio--;
                        }
                    }
                    mappa.put(view.getPlayer().getNick(), punteggio);
                    if (punteggio > max) {
                        max = punteggio;
                        winners.clear();
                        winners.add(view.getPlayer());
                    }
                    if (punteggio == max) {
                        winners.add(view.getPlayer());
                    }
                }

                if (winners.size() > 1) {

                    int playerPosition = views.size() - 1;
                    int punteggio = -1000;

                    boolean tentativo = true;
                    for (Player winner : winners) {
                        int temp = model.getPrivateObjective(winner).getPoints(model.getBoard(winner));
                        if (temp > punteggio) {
                            punteggio = temp;
                            vincitore = winner.getNick();
                        } else if (temp == punteggio) {
                            tentativo = false;
                        }
                    }
                    if (!tentativo) {
                        punteggio = -1000;
                        tentativo = true;
                        for (Player winner : winners) {
                            if (winner.getFavorTokens() > punteggio) {
                                punteggio = winner.getFavorTokens();
                                vincitore = winner.getNick();
                            } else if (winner.getFavorTokens() == punteggio)
                                tentativo = false;
                        }
                    }
                    if (!tentativo) {
                        while (playerPosition >= 0) {
                            if (winners.contains(model.getPlayers().get(playerPosition))) {
                                vincitore = model.getPlayers().get(playerPosition).getNick();
                                break;
                            } else {
                                playerPosition--;
                            }
                        }
                    }
                } else
                    vincitore = winners.get(0).getNick();
                for (RemoteView view : views) {
                    view.elaborateMessage(new ServerMessage(mappa, vincitore));
                    view.connectionClosed();
                }
            }
            model.deleteObservers();
            server.deregisterConnections(views);

            LOGGER.log(Level.FINE, "Endgame");
        }
    }

    /**
     * End the round and update the round variables
     */
    private synchronized void endRound() {

        if (getModel().getRound() == 9)
            endGame(false);
        else {
            getModel().setTurn(0);
            getModel().setFirstTurn(true);
            getModel().getRoundTrack().addDice(getModel().getRound(), getModel().getDraftPool().removeAll());
            Collections.rotate(getModel().getPlayers(), -1);
            getModel().getDraftPool().rollDice(getModel().getDiceBag());
        }
        getModel().setRound(getModel().getRound() + 1);

    }

    /**
     * Check if there are more than one player still playing
     * @return true if the count of connected players is > 1 false otherwise
     */
    public boolean playerCountCheck(){
        long p;

        p = views.stream().filter(k -> k.isConnected()).count();

        if (p > 1)
            return true;
        return false;
    }
    /**
     * Change the current player turn
     */
    public synchronized void nextTurn() {

        LOGGER.log(Level.FINE, "Next turn chiamato");
        model.setUsedTool(false);
        model.setNormalMove(false);
        int playerPosition = (model.getTurn() < model.getPlayers().size()) ? model.getTurn() : model.getPlayers().size() * 2 - model.getTurn() - 1;
        if (playerPosition > model.getPlayers().size() - 1) {
            LOGGER.log(Level.SEVERE, "Player position = " + playerPosition + " Player Size = " + model.getPlayers().size());
        }
        model.getPlayers().get(playerPosition).setYourTurn(false);
        model.setTurn(model.getTurn() + 1);

        if (model.getTurn() == model.getPlayers().size() * 2)
            endRound();
        else if (model.getTurn() == model.getPlayers().size())
            model.setFirstTurn(false);


        if (model.getRound() != 10) {

            int playerPosition2 = (model.getTurn() < model.getPlayers().size()) ? model.getTurn() : model.getPlayers().size() * 2 - model.getTurn() - 1;
            if (playerPosition2 > model.getPlayers().size() - 1) {
                LOGGER.log(Level.SEVERE, "Player position2 = " + playerPosition2 + " Player Size = " + model.getPlayers().size());
            }
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
            endGame(true);
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
                            if (!gameEnded)
                                model.notifyObs();
                        }

                    } catch (InvalidParameterException e) {
                        LOGGER.log(Level.SEVERE, "Ricevuta PlayerMove con parametri invalidi");
                        remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                    }

                } else if (message.getMessageType() == MessageType.CHOSENBOARD) {
                    LOGGER.log(Level.INFO, "Board ricevuta");
                    choosenBoards.put(remoteView.getPlayer(), new PlayerBoard(message.getBoardName()));
                    notifyAll();
                } else if (message.getMessageType() == MessageType.HASDISCONNECTED) {
                    if (!gameEnded) {
                        LOGGER.log(Level.INFO, "Notifico la disconnessione di " + remoteView.getPlayer().getNick());
                        for (RemoteView view : views) {
                            view.elaborateMessage(new ServerMessage(MessageType.HASDISCONNECTED, remoteView.getPlayer().getNick()));
                        }
                        if (remoteView.getPlayer().isYourTurn()) {

                            LOGGER.log(Level.FINE, "Era il turno del giocatore disconnesso, passo al nuovo turno");
                            nextTurn();
                            setTimer(model.getTurn(), model.getRound());
                            if (!gameEnded)
                                getModel().notifyObs();

                        }
                    }
                } else if (message.getMessageType() == MessageType.HASRICONNECTED) {
                    LOGGER.log(Level.INFO, remoteView.getPlayer().getNick() + " si è riconnesso");
                    for (RemoteView view : views) {
                        view.elaborateMessage(new ServerMessage(MessageType.HASRICONNECTED, remoteView.getPlayer().getNick()));
                    }
                    remoteView.elaborateMessage(new ServerMessage(MessageType.INITIALCONFIGSERVER, new ModelView(model, model.getPrivateObjective(remoteView.getPlayer()))));
                }
            } catch (ClassCastException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
    }

}
