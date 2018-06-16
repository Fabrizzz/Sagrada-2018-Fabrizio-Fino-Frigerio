package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.messages.PlayerMove;

import java.util.logging.Logger;


public abstract class Handler {

    protected Handler nextHandler;
    protected static final Logger LOGGER = Logger.getLogger("Logger");

    protected Handler() {

    }

    public Handler setNextHandler(Handler handler) {
        nextHandler = handler;
        return nextHandler;
    }

    public abstract void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException;
}
