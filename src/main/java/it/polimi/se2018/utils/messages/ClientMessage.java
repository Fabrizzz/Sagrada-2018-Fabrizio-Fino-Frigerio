package it.polimi.se2018.utils.messages;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.utils.enums.MessageType;

public class ClientMessage extends Message {
    private PlayerMove playerMove;
    private String nick;
    private Long id;
    private Board board;

    public ClientMessage(PlayerMove playerMove) {
        super(MessageType.PLAYERMOVE);
        this.playerMove = playerMove;
    }

    public ClientMessage(String nick, Long id) {
        super(MessageType.INITIALCONFIG);
        this.nick = nick;
        this.id = id;
    }

    public ClientMessage(Board board) {
        super(MessageType.CHOSENBOARD);
        this.board = board;
    }


    public PlayerMove getPlayerMove() {
        return playerMove;
    }

    public String getNick() {
        return nick;
    }

    public Long getId() {
        return id;
    }

    public Board getBoardName() {
        return board;
    }

}
