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
            if (!playerMove.getRow().isPresent() || !playerMove.getColumn().isPresent() || !playerMove.getDraftPosition().isPresent() ||
                    playerMove.getRow().orElse(0) < 0 || playerMove.getRow().orElse(0) > 3 || playerMove.getColumn().orElse(0) < 0 ||
                    playerMove.getColumn().orElse(0) > 4 || playerMove.getDraftPosition().orElse(0) < -1 || playerMove.getDraftPosition().orElse(0) >= model.getDraftPool().size()) {
                LOGGER.log(Level.WARNING,"Errore parametri RIGAINSUGHERO");
                throw new InvalidParameterException();
            }
            try {
                board = model.getBoard(remoteView.getPlayer());
                row = playerMove.getRow().orElse(0);
                column = playerMove.getColumn().orElse(0);
                pos = playerMove.getDraftPosition().orElse(0);
                die = model.getDraftPool().getDie(pos);
                if ((board.isEmpty() && !board.verifyInitialPositionRestriction(row, column)) || (!board.isEmpty() && (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) ||
                        board.containsDie(row, column) ||
                        !board.verifyColorRestriction(die, row, column) ||
                        !board.verifyNumberRestriction(die, row, column) ||
                        !board.verifyNearCellsRestriction(die, row, column) ||
                        board.verifyPositionRestriction(row, column)))) {
                    LOGGER.log(Level.INFO, "Il giocatore non puo' utilizzare RIGAINSUGHERO");
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                } else {

                    board.setDie(die, row, column);
                    model.getDraftPool().removeDie(die);
                    completeTool(remoteView.getPlayer(), model, playerMove.getTool());

                    return true;
                }
            } catch (NoDieException e) {
                LOGGER.log(Level.SEVERE, "Dado non presente in RIGAINSUGHERO");
            } catch (AlredySetDie alredySetDie) {
                LOGGER.log(Level.SEVERE, "Dado gia' presente in RIGAINSUGHERO");
            }
        } else {
            LOGGER.log(Level.FINEST, "La mossa non e' RIGAINSUGHERO, passaggio responsabilita' all'handler successivo");
            return this.nextHandler.process(playerMove, remoteView, model);
        }
        return false;
    }
}
