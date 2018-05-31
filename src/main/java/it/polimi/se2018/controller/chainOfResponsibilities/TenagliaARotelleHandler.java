package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.ServerMessage;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;

public class TenagliaARotelleHandler extends ToolHandler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        if (playerMove.getTool() == Tool.TENAGLIAAROTELLE) {
            if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) || !model.isFirstTurn())
                remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
            else {
                if (model.hasUsedNormalMove())
                    model.setNormalMove(false);
                else
                    remoteView.getPlayer().setCanDoTwoTurn(true);
                remoteView.getPlayer().setSkipSecondTurn(true);
                completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                nextHandler.process(playerMove, remoteView, model);
            }
        } else
            nextHandler.process(playerMove, remoteView, model);
    }
}
