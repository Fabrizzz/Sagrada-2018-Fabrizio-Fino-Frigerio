package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.enums.Tool;

public class EndOfTurnHandler extends Handler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) {
        if ((playerMove.getTool() == Tool.SKIPTURN) || (model.hasUsedTool() && model.hasUsedNormalMove())) {
            model.nextTurn();
        }
    }
}
