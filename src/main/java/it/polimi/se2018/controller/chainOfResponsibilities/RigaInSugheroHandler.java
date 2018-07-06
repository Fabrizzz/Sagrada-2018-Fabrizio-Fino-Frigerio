package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlreadySetDie;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.Optional;
import java.util.logging.Level;

public class RigaInSugheroHandler extends ToolHandler {

    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        PlayerBoard board;
        int row;
        int column;
        int pos;
        Die die;

        if (playerMove.getTool() == Tool.RIGAINSUGHERO) {
            LOGGER.log(Level.FINE,"Elaborazione validita' mossa RIGAINSUGHERO");
            Optional<Integer> rowO = playerMove.getRow();
            Optional<Integer> colO = playerMove.getColumn();
            Optional<Integer> posO = playerMove.getDraftPosition();
            if (rowO.isPresent() && colO.isPresent() && posO.isPresent()) {
                row = rowO.get();
                column = colO.get();
                pos = posO.get();
            }else{
                LOGGER.log(Level.SEVERE,"Errore parametri assenti MOSSASTANDARD");
                throw new InvalidParameterException();
            }

            if (row < 0 || row > 3 || column < 0 || column > 4 || pos >= model.getDraftPool().size()) {
                LOGGER.log(Level.WARNING,"Errore parametri RIGAINSUGHERO");
                throw new InvalidParameterException();
            }
            try {
                board = model.getBoard(remoteView.getPlayer());
                die = model.getDraftPool().getDie(pos);
                if ((board.isEmpty() && !board.verifyInitialPositionRestriction(row, column)) ||
                        cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) ||
                        board.containsDie(row, column) ||
                        !board.verifyColorRestriction(die, row, column) ||
                        !board.verifyNumberRestriction(die, row, column) ||
                        !board.verifyNearCellsRestriction(die, row, column) ||
                        board.verifyPositionRestriction(row, column)) {
                    LOGGER.log(Level.INFO, "Il giocatore non puo' utilizzare RIGAINSUGHERO");
                    remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                    return false;
                } else {

                    board.setDie(die, row, column);
                    model.getDraftPool().removeDie(die);
                    completeTool(remoteView.getPlayer(), model, playerMove.getTool());

                    return true;
                }
            } catch (NoDieException e) {
                LOGGER.log(Level.SEVERE, "Dado non presente in RIGAINSUGHERO");
                remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                return false;
            } catch (AlreadySetDie alreadySetDie) {
                LOGGER.log(Level.SEVERE, "Dado gia' presente in RIGAINSUGHERO");
                remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                return false;
            }
        } else {
            LOGGER.log(Level.FINEST, "La mossa non e' RIGAINSUGHERO, passaggio responsabilita' all'handler successivo");
            return this.nextHandler.process(playerMove, remoteView, model);
        }
    }
}
