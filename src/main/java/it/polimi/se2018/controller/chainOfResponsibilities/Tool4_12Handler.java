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

public class Tool4_12Handler extends ToolHandler {

    Tool tool;

    protected Tool4_12Handler(Tool tool) {
        this.tool = tool;
        if (tool != Tool.LATHEKIN && tool != Tool.TAGLIERINAMANUALE){
            LOGGER.log(Level.SEVERE,"Errore parametri LATHEKIN TAGLIERINAMANUALE");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        PlayerBoard board;
        int firstRow;
        int firstColumn;
        int firstFinalRow;
        int firstFinalColumn;
        int secondRow = -1;
        int secondColumn = -1;
        int secondFinalRow = -1;
        int secondFinalColumn = -1;
        PlayerMove playerMove2;

        if (playerMove.getTool() == Tool.LATHEKIN || playerMove.getTool() == Tool.TAGLIERINAMANUALE) {
            LOGGER.log(Level.FINE,"Elaborazione validita' mossa LATHEKIN TAGLIERINAMANUALE");
            Optional<Integer> rowO = playerMove.getRow();
            Optional<Integer> columnO =  playerMove.getColumn();
            Optional<Integer> finalRowO = playerMove.getFinalRow();
            Optional<Integer> finalColumnO =  playerMove.getFinalColumn();

            if (rowO.isPresent() && columnO.isPresent() && finalRowO.isPresent() && finalColumnO.isPresent()) {
                firstRow = rowO.get();
                firstColumn = columnO.get();
                firstFinalColumn = finalColumnO.get();
                firstFinalRow = finalRowO.get();


                Optional<PlayerMove> tmpPlayerMove = playerMove.getNextMove();
                if (tmpPlayerMove.isPresent()) {
                    playerMove2 = tmpPlayerMove.get();

                    Optional<Integer> secondRowO = playerMove2.getRow();
                    Optional<Integer> secondColumnO = playerMove2.getColumn();
                    Optional<Integer> secondFinalRowO = playerMove2.getFinalRow();
                    Optional<Integer> secondFinalColumnO = playerMove2.getFinalColumn();

                    if (secondRowO.isPresent() && secondColumnO.isPresent() && secondFinalRowO.isPresent() && secondFinalColumnO.isPresent()) {
                        secondRow = secondRowO.get();
                        secondColumn = secondColumnO.get();
                        secondFinalColumn = secondFinalColumnO.get();
                        secondFinalRow = secondFinalRowO.get();


                        if (secondColumn < 0 || secondColumn > 4 || secondRow < 0 || secondRow > 3 ||
                                secondFinalColumn < 0 || secondFinalColumn > 4 || secondFinalRow < 0 || secondFinalRow > 3) {
                            LOGGER.log(Level.SEVERE, "Errore parametri LATHEKIN TAGLIERINAMANUALE");
                            throw new InvalidParameterException();
                        }
                    }else{
                        LOGGER.log(Level.SEVERE, "Errore parametri LATHEKIN TAGLIERINAMANUALE");
                        throw new InvalidParameterException();
                    }
                }
                board = model.getBoard(remoteView.getPlayer());

                try {
                    Die die1;
                    Die die2;
                    boolean check = true;

                    if (board.containsDie(firstRow, firstColumn)) {
                        die1 = board.getDie(firstRow, firstColumn);
                        if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) ||
                                board.containsDie(firstFinalRow, firstFinalColumn) ||
                                !board.verifyPositionRestriction(firstFinalRow, firstFinalColumn) ||
                                !board.verifyNearCellsRestriction(die1, firstFinalRow, firstFinalColumn) ||
                                !board.verifyColorRestriction(die1, firstFinalRow, firstFinalColumn) ||
                                !board.verifyNumberRestriction(die1, firstFinalRow, firstFinalColumn))
                            check = false;
                    } else
                        check = false;

                    if (playerMove.getNextMove().isPresent()) {
                        die2 = board.getDie(secondRow, secondColumn);
                        if (board.containsDie(secondRow, secondColumn)) {

                            if (board.containsDie(secondFinalRow, secondFinalColumn) ||
                                    !board.verifyPositionRestriction(secondFinalRow, secondFinalColumn) ||
                                    !board.verifyNearCellsRestriction(die2, secondFinalRow, secondFinalColumn) ||
                                    !board.verifyColorRestriction(die2, secondFinalRow, secondFinalColumn) ||
                                    !board.verifyNumberRestriction(die2, secondFinalRow, secondFinalColumn))
                                check = false;

                        } else
                            check = false;

                    }

                    if (check) {
                        die1 = board.getDie(firstRow, firstColumn);
                        board.removeDie(firstRow, firstColumn);
                        board.setDie(die1, firstRow, firstColumn);
                        if (playerMove.getNextMove().isPresent()) {
                            die2 = board.getDie(secondRow, secondColumn);
                            board.removeDie(secondRow, secondColumn);
                            board.setDie(die2, secondFinalRow, secondFinalColumn);
                        }
                        return true;
                    } else {
                        LOGGER.log(Level.INFO, "Il giocatore non puo' utilizzare LATHEKIN TAGLIERINAMANUALE");
                        remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                    }
                } catch (AlredySetDie alredySetDie) {
                    LOGGER.log(Level.SEVERE, "Dado gia' presente in LATHEKIN TAGLIERINAMANUALE");
                } catch (NoDieException e) {
                    LOGGER.log(Level.SEVERE, "Dado non presente in LATHEKIN TAGLIERINAMANUALE");
                }
            }else{
                LOGGER.log(Level.SEVERE,"Errore parametri LATHEKIN TAGLIERINAMANUALE");
                throw new InvalidParameterException();
            }
        } else{
            LOGGER.log(Level.FINEST, "La mossa non e' LATHEKIN TAGLIERINAMANUALE, passaggio responsabilita' all'handler successivo");
            return nextHandler.process(playerMove, remoteView, model);
        }
        return false;
    }
}

