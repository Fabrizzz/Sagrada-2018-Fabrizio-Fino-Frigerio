package it.polimi.se2018.utils.messages;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.MessageType;

public class ServerMessage extends Message {
    private ErrorType errorType;
    private ModelView modelView;
    private String disconnectedPlayer;
    private Board[] boards;

    public ServerMessage(String player) {
        super(MessageType.HASDISCONNECTED);
        this.disconnectedPlayer = player;
    }


    public ServerMessage(ErrorType errorType) {
        super(MessageType.ERROR);
        this.errorType = errorType;
    }

    public ServerMessage(MessageType messageType, ModelView modelView) {
        super(messageType);
        this.modelView = modelView;
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
}
