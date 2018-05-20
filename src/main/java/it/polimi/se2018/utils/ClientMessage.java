package it.polimi.se2018.utils;

import it.polimi.se2018.utils.enums.BoardName;
import it.polimi.se2018.utils.enums.MessageType;

import java.util.Optional;

public class ClientMessage extends Message {
    private Optional<PlayerMove> playerMove;
    private Optional<String> nick;
    private Optional<Long> id;
    private Optional<BoardName> boardName;

    public ClientMessage(PlayerMove playerMove) {
        super(MessageType.PLAYERMOVE);
        this.playerMove = Optional.of(playerMove);
    }

    public ClientMessage(String nick, Long id) {
        super(MessageType.INITIALCONFIG);
        this.nick = Optional.of(nick);
        this.id = Optional.of(id);
    }

    public ClientMessage(BoardName boardName) {
        super(MessageType.CHOSENBOARD);
        this.boardName = Optional.of(boardName);
    }


    public Optional<PlayerMove> getPlayerMove() {
        return playerMove;
    }

    public Optional<String> getNick() {
        return nick;
    }

    public Optional<Long> getId() {
        return id;
    }

    public Optional<BoardName> getBoardName() {
        return boardName;
    }

}
