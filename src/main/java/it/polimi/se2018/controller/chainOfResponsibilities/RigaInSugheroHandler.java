package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

public class RigaInSugheroHandler extends ToolHandler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        PlayerBoard board;
        int row;
        int column;
        int pos;
        Die die;

        if (playerMove.getTool() == Tool.RIGAINSUGHERO) {
            if (!playerMove.getRow().isPresent() || !playerMove.getColumn().isPresent() || !playerMove.getDraftPosition().isPresent())
                throw new InvalidParameterException();
            try {
                board = model.getBoard(remoteView.getPlayer());
                row = playerMove.getRow().get();
                column = playerMove.getColumn().get();
                pos = playerMove.getDraftPosition().get();
                die = model.getDraftPool().getDie(pos);
                if (
                        cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) ||
                                (board.isEmpty() && !board.verifyInitialPositionRestriction(row, column)) ||
                                board.containsDie(row, column) ||
                                !board.verifyColorRestriction(die, row, column) ||
                                !board.verifyNumberRestriction(die, row, column) ||
                                !board.verifyNearCellsRestriction(die, row, column) ||
                                board.verifyPositionRestriction(row, column))

                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                else {

                    board.setDie(die, row, column);
                    model.getDraftPool().removeDie(die);
                    completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                    nextHandler.process(playerMove, remoteView, model);

                }
            } catch (NoDieException e) {
                e.printStackTrace();
            } catch (AlredySetDie alredySetDie) {
                alredySetDie.printStackTrace();
            }
        } else
            this.nextHandler.process(playerMove, remoteView, model);
    }
}
