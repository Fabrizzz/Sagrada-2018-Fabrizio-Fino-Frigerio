package it.polimi.se2018.utils.messages;


import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.enums.MessageType;

public class ChosenBoardMessage extends Message{
    private PlayerBoard chosenBoard;

    public ChosenBoardMessage(MessageType messageType, PlayerBoard chosenBoard) {
        super(messageType);
        this.chosenBoard = chosenBoard;
    }

    public PlayerBoard getPlayerBoard() {
        return chosenBoard;
    }
}
