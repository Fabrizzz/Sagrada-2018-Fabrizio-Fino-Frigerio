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

import java.util.logging.Level;

public class TamponeDiamantatoHandler extends ToolHandler {


    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        int row;
        int column;
        PlayerBoard playerBoard;
        if (playerMove.getTool() == Tool.TAMPONEDIAMANTATO) {
            LOGGER.log(Level.FINE,"Elaborazione validita' mossa TAMPONEDIAMANTATO");
            if (!playerMove.getColumn().isPresent() || !playerMove.getRow().isPresent()){
                LOGGER.log(Level.SEVERE,"Errore parametri TAMPONEDIAMANTATO");
                throw new InvalidParameterException();
            }

            row = playerMove.getRow().orElse(0);
            column = playerMove.getColumn().orElse(0);
            playerBoard = model.getBoard(remoteView.getPlayer());
            if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) || !playerBoard.containsDie(row, column)){
                LOGGER.log(Level.INFO, "Il giocatore non puo' utilizzare TAMPONEDIAMANTATO");
                remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
            } else {
                try {
                    playerBoard.getDie(row, column).flip();
                    completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                    nextHandler.process(playerMove, remoteView, model);
                } catch (NoDieException e) {
                    LOGGER.log(Level.SEVERE, "Dado non presente in TAMPONEDIAMANTATO");
                }
            }
        } else {
            LOGGER.log(Level.FINEST, "La mossa non e' TAMPONEDIAMANTATO, passaggio responsabilita' all'handler successivo");
            nextHandler.process(playerMove, remoteView, model);
        }
    }
}
