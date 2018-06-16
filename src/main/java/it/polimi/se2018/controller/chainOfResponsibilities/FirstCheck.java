package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FirstCheck extends Handler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {
        int temp;
        LOGGER.log(Level.FINE,"FirstCheck della chain of responsabilities");
        if (remoteView.getPlayer().isYourTurn()) {
            if (playerMove.getColumn().filter(k -> k < 0 || k > 4).isPresent() ||
                    playerMove.getDraftPosition().filter(k -> (k >= model.getDraftPool().size())).isPresent() ||
                    playerMove.getFinalColumn().filter(k -> k < 0 || k > 4).isPresent() ||
                    playerMove.getFinalRow().filter(k -> k < 0 || k > 3).isPresent() ||
                    playerMove.getRoundTrackRound().filter(k -> k >= model.getRound()).isPresent() ||
                    playerMove.getRow().filter(k -> k < 0 || k > 3).isPresent() ||
                    (!model.getTools().containsKey(playerMove.getTool()) &&
                            playerMove.getTool() != Tool.MOSSASTANDARD &&
                            playerMove.getTool() != Tool.SKIPTURN))

                throw new InvalidParameterException();
            else{
                nextHandler.process(playerMove, remoteView, model);
            }


        } else {
            LOGGER.log(Level.FINE,"Non e' il turno del giocatore, invio messaggio NOTYOURTURN");
            remoteView.sendBack(new ServerMessage(ErrorType.NOTYOURTURN));
        }

    }
}
