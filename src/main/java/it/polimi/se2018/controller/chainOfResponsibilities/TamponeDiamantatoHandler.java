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

public class TamponeDiamantatoHandler extends ToolHandler {


    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        int row, column;
        PlayerBoard playerBoard;
        if (playerMove.getTool() == Tool.TAMPONEDIAMANTATO) {
            if (!playerMove.getColumn().isPresent() || !playerMove.getRow().isPresent())
                throw new InvalidParameterException();
            row = playerMove.getRow().get();
            column = playerMove.getColumn().get();
            playerBoard = model.getBoard(remoteView.getPlayer());
            if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) || !playerBoard.containsDie(row, column))
                remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
            else {
                try {
                    playerBoard.getDie(row, column).flip();
                    completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                    nextHandler.process(playerMove, remoteView, model);
                } catch (NoDieException e) {
                    e.printStackTrace();
                }
            }
        } else {
            nextHandler.process(playerMove, remoteView, model);
        }
    }
}
