package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.PlayerMove;


public abstract class Handler {

    protected Handler nextHandler;

    public void setNextHandler(Handler handler) {
        nextHandler = handler;
    }

    public abstract void process(PlayerMove playerMove, RemoteView remoteView, Model model);
}
