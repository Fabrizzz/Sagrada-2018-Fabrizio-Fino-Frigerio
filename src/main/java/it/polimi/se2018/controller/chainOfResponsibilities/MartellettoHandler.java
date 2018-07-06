package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.logging.Level;

public class MartellettoHandler extends ToolHandler {

    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        DraftPool draftPool;

        if (playerMove.getTool() == Tool.MARTELLETTO) {
            LOGGER.log(Level.FINE,"Elaborazione validita' mossa MARTELLETTO");
            if (model.isFirstTurn() || model.hasUsedNormalMove() || cantUseTool(remoteView.getPlayer(), model, playerMove.getTool())) {
                LOGGER.log(Level.INFO,"Il giocatore non puo' utilizzare la mossa MARTELLETTO");
                remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
            } else {
                model.getDraftPool().reRollDice();
                completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                return true;
            }
        } else{
            LOGGER.log(Level.FINEST,"La mossa non e' MARTELLETTO, passaggio responsabilita' all'handler successivo");
            return nextHandler.process(playerMove, remoteView, model);
        }
        return false;
    }
}
