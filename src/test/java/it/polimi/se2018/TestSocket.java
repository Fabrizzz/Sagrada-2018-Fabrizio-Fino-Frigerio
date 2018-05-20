package it.polimi.se2018;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.server.ServerNetwork;
import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.Message;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.enums.Tool;
import org.junit.Before;
import org.junit.Test;

import java.rmi.Remote;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestSocket {
    private RemoteView remoteView;
    private ServerNetwork serverNetwork;
    private Controller controller;
    private ClientNetwork clientNetwork;

    @Before
    public void initialize(){
        remoteView = new RemoteView();
        serverNetwork = new ServerNetwork(remoteView);

        controller = new Controller();
        clientNetwork = new ClientNetwork(controller);
    }

    @Test
    public void socketConnectionTest(){
        assertTrue(clientNetwork.connectSocket("localhost",8421));
        assertFalse(clientNetwork.connectSocket("localhost",8421));
    }

    /*@Test
    public void socketTransmissionTest(){
        assertTrue(clientNetwork.connectSocket("localhost",8421));
        Tool tool = Tool.SKIPTURN;
        PlayerMove playerMove = new PlayerMove(tool);
        Message message = new ClientMessage(playerMove);
        assertTrue(clientNetwork.sendMessage(message));
    }

    /*@Test
    public void RMIConnectionTest(){
        assertTrue(clientNetwork.connectRMI("localhost"));
    }*/
}
