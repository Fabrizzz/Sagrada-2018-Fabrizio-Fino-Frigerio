package it.polimi.se2018.utils;

import it.polimi.se2018.utils.enums.MessageType;

public abstract class Message {
    private MessageType messageType;

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
