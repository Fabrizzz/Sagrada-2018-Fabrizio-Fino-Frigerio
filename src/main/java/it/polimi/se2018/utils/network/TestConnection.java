package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.messages.Message;

import java.util.Observable;

public class TestConnection  extends Connection {
    private boolean sent = false;

    @Override
    public boolean sendMessage(Message message) {
        sent = true;
        return true;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void close() {

    }

    @Override
    public void update(Observable o, Object arg) {
        sent = true;
    }

    public boolean isSent() {
        return sent;
    }
}
