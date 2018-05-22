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


    public Optional<PlayerMove> getPlayerMove() {
        return Optional.ofNullable(playerMove);
    }

    public Optional<String> getNick() {
        return Optional.ofNullable(nick);
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<BoardName> getBoardName() {
        return Optional.ofNullable(boardName);
    }

}
