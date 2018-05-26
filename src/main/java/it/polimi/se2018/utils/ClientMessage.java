package it.polimi.se2018.utils;

import it.polimi.se2018.utils.enums.BoardName;
import it.polimi.se2018.utils.enums.MessageType;

import java.util.Optional;

public class ClientMessage extends Message {
    private PlayerMove playerMove;
    private String nick;
    private Long id;
    private BoardName boardName;

    public ClientMessage(PlayerMove playerMove) {
        super(MessageType.PLAYERMOVE);
        this.playerMove = playerMove;
    }

    public ClientMessage(String nick, Long id) {
        super(MessageType.INITIALCONFIG);
        this.nick = nick;
        this.id = id;
    }

    public ClientMessage(BoardName boardName) {
        super(MessageType.CHOSENBOARD);
        this.boardName = boardName;
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

    public BoardName getBoardName() {
        return boardName;
    }

}
