package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.logging.Level;

public class TenagliaARotelleHandler extends ToolHandler {
    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        if (playerMove.getTool() == Tool.TENAGLIAAROTELLE) {
            LOGGER.log(Level.FINE,"Elaborazione validita' mossa TENAGLIAAROTELLE");
            if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) || !model.isFirstTurn()){
                LOGGER.log(Level.INFO, "Il giocatore non puo' utilizzare TENAGLIAAROTELLE");
                remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                return false;
            }

            else {
                if (model.hasUsedNormalMove())
                    model.setNormalMove(false);
                else
                    remoteView.getPlayer().setCanDoTwoTurn(true);
                remoteView.getPlayer().setSkipSecondTurn(true);
                completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                return true;
            }
        } else{
            LOGGER.log(Level.FINEST, "La mossa non e' TENAGLIAAROTELLE, passaggio responsabilita' all'handler successivo");
            return nextHandler.process(playerMove, remoteView, model);
        }
    }
}
