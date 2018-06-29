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

import java.util.Optional;
import java.util.logging.Level;

public class NormalMoveHandler extends Handler {

    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {
        PlayerBoard board;
        int row;
        int column;
        int pos;
        Die die;

        if (playerMove.getTool() == Tool.MOSSASTANDARD) {
            LOGGER.log(Level.FINE,"Elaborazione validita' MOSSASTANDARD");
            try {
                board = model.getBoard(remoteView.getPlayer());
                Optional<Integer> rowO = playerMove.getRow();
                Optional<Integer> colO = playerMove.getColumn();
                Optional<Integer> posO = playerMove.getDraftPosition();
                if (rowO.isPresent() && colO.isPresent() && posO.isPresent()) {
                    row = rowO.get();
                    column = colO.get();
                    pos = posO.get();
                }else{
                    LOGGER.log(Level.SEVERE,"Errore parametri MOSSASTANDARD");
                    throw new InvalidParameterException();
                }

                die = model.getDraftPool().getDie(pos);
                if ((board.isEmpty() && !board.verifyInitialPositionRestriction(row, column)) ||
                        (!board.isEmpty() && (!board.verifyNearCellsRestriction(die, row, column) || !board.verifyPositionRestriction(row, column))) ||
                        model.hasUsedNormalMove() ||
                        board.containsDie(row, column) ||
                        !board.verifyColorRestriction(die, row, column) ||
                        !board.verifyNumberRestriction(die, row, column)
                        ) {
                    LOGGER.log(Level.INFO,"Il giocatore non puo' eseguire MOSSASTANDARD row: " + row + " column:" + column +
                            " colorRestriction: " + board.verifyColorRestriction(die, row, column) + " numberRestriction:" +
                            board.verifyNumberRestriction(die, row, column) + " nearCellREstriction:" + board.verifyNearCellsRestriction(die, row, column) +
                            " positionRestriction:" + board.verifyPositionRestriction(row, column) + " isEmpty: " + board.isEmpty() + " containsDie:" +  board.containsDie(row, column) +
                            " usedNormalMove:" + model.hasUsedNormalMove());
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                } else {

                    board.setDie(die, row, column);
                    model.getDraftPool().removeDie(die);

                    if (remoteView.getPlayer().isCanDoTwoTurn()){
                        LOGGER.log(Level.FINE,"Player puo' fare due turni");
                        remoteView.getPlayer().setCanDoTwoTurn(false);
                    }else {
                        LOGGER.log(Level.FINE,"Player ha usato normal move");
                        model.setNormalMove(true);
                    }
                    return true;
                }

            } catch (NoDieException e) {
                LOGGER.log(Level.SEVERE, "Dado non presente in MOSSASTANDARD");
            } catch (AlredySetDie alredySetDie) {
                LOGGER.log(Level.SEVERE, "Dado gia' presente in MOSSASTANDARD");
            }
        } else{
            LOGGER.log(Level.FINEST,"La mossa non e' MOSSASTANDARD, passaggio responsabilita' all'handler successivo");
            return this.nextHandler.process(playerMove, remoteView, model);
        }
        return false;
    }
}
