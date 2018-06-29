package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.Optional;
import java.util.logging.Level;

public class TamponeDiamantatoHandler extends ToolHandler {


    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        int draftPosition;
        if (playerMove.getTool() == Tool.TAMPONEDIAMANTATO) {
            LOGGER.log(Level.FINE,"Elaborazione validita' mossa TAMPONEDIAMANTATO");
            Optional<Integer> draftPositionO = playerMove.getDraftPosition();
            if(draftPositionO.isPresent()){
                draftPosition = draftPositionO.get();
            }else{
                LOGGER.log(Level.SEVERE,"Errore parametri assenti TAMPONEDIAMANTATO");
                throw new InvalidParameterException();
            }

            if (draftPosition < 0 || draftPosition >= model.getDraftPool().size()){
                LOGGER.log(Level.SEVERE,"Errore parametri TAMPONEDIAMANTATO");
                throw new InvalidParameterException();
            }

            if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool())){
                LOGGER.log(Level.INFO, "Il giocatore non puo' utilizzare TAMPONEDIAMANTATO");
                remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
            } else {
                try {
                    model.getDraftPool().getDie(draftPosition).flip();
                    completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                    return true;
                } catch (NoDieException e) {
                    LOGGER.log(Level.SEVERE, "Dado non presente in TAMPONEDIAMANTATO");
                }
            }
        } else {
            LOGGER.log(Level.FINEST, "La mossa non e' TAMPONEDIAMANTATO, passaggio responsabilita' all'handler successivo");
            return nextHandler.process(playerMove, remoteView, model);
        }
        return false;
    }
}
