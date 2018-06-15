package it.polimi.se2018.utils.messages;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.utils.enums.MessageType;

public class SelectBoardMessage extends Message {
    private Board[] boards;
    public SelectBoardMessage(MessageType messageType, Board[] boards) {
        super(messageType);
        this.boards = boards;
    }

    public Board[] getBoards() {
        return boards;
    }
}
