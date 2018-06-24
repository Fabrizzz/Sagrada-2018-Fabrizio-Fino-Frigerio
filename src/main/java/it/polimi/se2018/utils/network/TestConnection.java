package it.polimi.se2018.utils.network;

import it.polimi.se2018.utils.messages.Message;

import java.util.Observable;

public class TestConnection  extends Connection {
    @Override
    public boolean sendMessage(Message message) {
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

    }
}
