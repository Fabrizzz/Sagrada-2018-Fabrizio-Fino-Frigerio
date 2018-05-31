package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.ServerMessage;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;

public class MartellettoHandler extends ToolHandler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        DraftPool draftPool;

        if (playerMove.getTool() == Tool.MARTELLETTO) {
            if (model.isFirstTurn() || model.hasUsedNormalMove() || cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()))
                remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
            else {
                draftPool = model.getDraftPool();
                draftPool.reRollDice();
                completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                nextHandler.process(playerMove, remoteView, model);
            }
        } else
            nextHandler.process(playerMove, remoteView, model);
    }
}
