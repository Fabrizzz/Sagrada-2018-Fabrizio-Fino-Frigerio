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

public class NormalMoveHandler extends Handler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {
        PlayerBoard board;
        int row;
        int column;
        int pos;
        Die die;

        if (playerMove.getTool() == Tool.MOSSASTANDARD) {
            LOGGER.log(Level.FINE,"Elaborazione validita' MOSSASTANDARD");
            if (!playerMove.getRow().isPresent() || !playerMove.getColumn().isPresent() || !playerMove.getDraftPosition().isPresent()){
                LOGGER.log(Level.SEVERE,"Errore parametri MOSSASTANDARD");
                throw new InvalidParameterException();
            }
            try {
                board = model.getBoard(remoteView.getPlayer());
                row = playerMove.getRow().orElse(0);
                column = playerMove.getColumn().orElse(0);
                pos = playerMove.getDraftPosition().orElse(0);
                die = model.getDraftPool().getDie(pos);
                if ((board.isEmpty() && !board.verifyInitialPositionRestriction(row, column)) || ((!board.isEmpty()) && (model.hasUsedNormalMove() ||
                        board.containsDie(row, column) ||
                        !board.verifyColorRestriction(die, row, column) ||
                        !board.verifyNumberRestriction(die, row, column) ||
                        !board.verifyNearCellsRestriction(die, row, column) ||
                        !board.verifyPositionRestriction(row, column)))) {
                    LOGGER.log(Level.INFO,"Il giocatore non puo' eseguire MOSSASTANDARD row: " + row + " column:" + column +
                    " colorRestriction: " + board.verifyColorRestriction(die, row, column) + " numberRestriction:" +
                            board.verifyNumberRestriction(die, row, column) + " nearCellREstriction:" + board.verifyNearCellsRestriction(die, row, column) +
                    " positionRestriction:" + board.verifyPositionRestriction(row, column));
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
                    nextHandler.process(playerMove, remoteView, model);

                }
            } catch (NoDieException e) {
                LOGGER.log(Level.SEVERE, "Dado non presente in MOSSASTANDARD");
            } catch (AlredySetDie alredySetDie) {
                LOGGER.log(Level.SEVERE, "Dado gia' presente in MOSSASTANDARD");
            }
        } else{
            LOGGER.log(Level.FINEST,"La mossa non e' MOSSASTANDARD, passaggio responsabilita' all'handler successivo");
            this.nextHandler.process(playerMove, remoteView, model);
        }
    }
}
