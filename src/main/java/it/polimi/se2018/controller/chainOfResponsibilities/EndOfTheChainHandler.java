package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

public class EndOfTheChainHandler extends Handler {
    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {
        if (playerMove.getTool() == Tool.SKIPTURN)
            return true;
        remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
        return false;
    }
}
