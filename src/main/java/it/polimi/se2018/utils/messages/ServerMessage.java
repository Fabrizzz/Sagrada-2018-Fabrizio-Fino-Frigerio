package it.polimi.se2018.utils.messages;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.ModelViewUpdate;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.MessageType;

import java.util.Map;

public class ServerMessage extends Message {
    private ErrorType errorType;
    private ModelView modelView;
    private ModelViewUpdate update;
    private String disconnectedPlayer;
    private Board[] boards;
    private Map<String,Integer> scores;
    private String winner;

    public ServerMessage(MessageType messageType, String player) {
        super(messageType);
        this.disconnectedPlayer = player;
    }


    public ServerMessage(ErrorType errorType) {
        super(MessageType.ERROR);
        this.errorType = errorType;
    }

    public ServerMessage(ModelView modelView) {
        super(MessageType.INITIALCONFIGSERVER);
        this.modelView = modelView;
    }

    public ServerMessage(ModelViewUpdate update) {
        super(MessageType.MODELVIEWUPDATE);
        this.update = update;
    }

    public ServerMessage(Map<String, Integer> scores, String winner) {
        super(MessageType.ENDGAME);
        this.scores = scores;
        this.winner = winner;
    }

    public ServerMessage(Board[] boards) {
        super(MessageType.BOARDTOCHOOSE);
        this.boards = boards;

    }

    public ErrorType getErrorType(){
        return errorType;
    }

    public ModelView getModelView() {
        return modelView;
    }

    public Board[] getBoards() {
        return boards;
    }

    public String getDisconnectedPlayer() {
        return disconnectedPlayer;
    }

    public Map<String, Integer> getScores() { return scores; }

    public String getWinner() {
        return winner;
    }

    public ModelViewUpdate getUpdate() {
        return update;
    }
}

