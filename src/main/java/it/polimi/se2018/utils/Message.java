package it.polimi.se2018.utils;

import it.polimi.se2018.utils.enums.MessageType;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private MessageType messageType;

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
