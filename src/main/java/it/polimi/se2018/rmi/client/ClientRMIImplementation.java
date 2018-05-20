package it.polimi.se2018.rmi.client;

import it.polimi.se2018.utils.Connection;
import it.polimi.se2018.utils.Message;

import java.rmi.Remote;


public class ClientRMIImplementation implements Remote,Connection{

    public void sendMessage(Message message) {
        //gestione messaggio lato client
    }
}
