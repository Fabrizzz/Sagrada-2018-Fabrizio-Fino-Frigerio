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

public class Tool4_12Handler extends ToolHandler {

    Tool tool;

    public Tool4_12Handler(Tool tool) {
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

                Optional<PlayerMove> secondeMoveO = playerMove.getNextMove();
                if (secondeMoveO.isPresent()) {
                    playerMove2 = secondeMoveO.get();

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
                            LOGGER.log(Level.SEVERE, "Errore parametri LATHEKIN TAGLIERINAMANUALE 1");
                            throw new InvalidParameterException();
                        }
                    }else{
                        LOGGER.log(Level.SEVERE, "Errore parametri LATHEKIN TAGLIERINAMANUALE 2");
                        throw new InvalidParameterException();
                    }
                }else if(playerMove.getTool() == Tool.LATHEKIN){
                    LOGGER.log(Level.SEVERE, "Errore parametri LATHEKIN, seconda mossa assente 3");
                    throw new InvalidParameterException();
                }

                board = model.getBoard(remoteView.getPlayer());

                try {
                    Die die1;
                    Die die2;
                    if (board.containsDie(firstRow, firstColumn)) {
                        die1 = board.getDie(firstRow, firstColumn);
                        board.removeDie(firstRow,firstColumn);
                        if (tool == Tool.TAGLIERINAMANUALE && !model.getRoundTrack().hasColor(die1.getColor()) ||
                                cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) ||
                                (board.isEmpty() && !board.verifyInitialPositionRestriction(firstFinalRow, firstFinalColumn)) ||
                                !board.isEmpty() && (
                                        board.containsDie(firstFinalRow, firstFinalColumn) ||
                                                !board.verifyPositionRestriction(firstFinalRow, firstFinalColumn) ||
                                                !board.verifyNearCellsRestriction(die1, firstFinalRow, firstFinalColumn) ||
                                                !board.verifyColorRestriction(die1, firstFinalRow, firstFinalColumn) ||
                                                !board.verifyNumberRestriction(die1, firstFinalRow, firstFinalColumn))) {
                            board.setDie(die1,firstRow,firstColumn);
                            LOGGER.log(Level.FINE,"Restrizioni dado 1 non rispettate");
                            remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                            return false;
                        }
                    } else{
                        LOGGER.log(Level.FINE,"Dado 1 non presente");
                        remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                        return false;
                    }

                    board.setDie(die1, firstFinalRow, firstFinalColumn);

                    if (playerMove.getNextMove().isPresent()) {
                        if (board.containsDie(secondRow, secondColumn)) {
                            die2 = board.getDie(secondRow, secondColumn);
                            board.removeDie(secondRow,secondColumn);
                            if ((tool == Tool.TAGLIERINAMANUALE && (die1.getColor() != die2.getColor())) ||
                                    board.containsDie(secondFinalRow, secondFinalColumn) ||
                                    !board.verifyPositionRestriction(secondFinalRow, secondFinalColumn) ||
                                    !board.verifyNearCellsRestriction(die2, secondFinalRow, secondFinalColumn) ||
                                    !board.verifyColorRestriction(die2, secondFinalRow, secondFinalColumn) ||
                                    !board.verifyNumberRestriction(die2, secondFinalRow, secondFinalColumn)){
                                board.setDie(die2,secondRow,secondColumn);
                                board.removeDie(firstFinalRow,firstFinalColumn);
                                board.setDie(die1,firstRow,firstColumn);
                                LOGGER.log(Level.FINE,"Restrizioni dado 2 non rispettate: containsDie:" + board.containsDie(secondFinalRow, secondFinalColumn) +
                                " position restriction: " + secondFinalRow + " " +secondFinalColumn + " " + board.verifyPositionRestriction(secondFinalRow, secondFinalColumn));
                                remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                                return false;
                            }else{
                                board.setDie(die2, secondFinalRow, secondFinalColumn);
                                return true;
                            }
                        } else{
                            board.removeDie(firstFinalRow,firstFinalColumn);
                            board.setDie(die1,firstRow,firstColumn);
                            LOGGER.log(Level.FINE,"Dado 2 non presente in posizione " + secondRow + " " + secondColumn);
                            remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                            return false;
                        }
                    }else {
                        return true;
                    }
                } catch (AlreadySetDie alreadySetDie) {
                    LOGGER.log(Level.SEVERE, "Dado gia' presente in LATHEKIN TAGLIERINAMANUALE 5");
                    remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                    return false;
                } catch (NoDieException e) {
                    LOGGER.log(Level.SEVERE, "Dado non presente in LATHEKIN TAGLIERINAMANUALE 6");
                    remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                    return false;
                }
            }else{
                LOGGER.log(Level.SEVERE,"Errore parametri LATHEKIN TAGLIERINAMANUALE 7");
                throw new InvalidParameterException();
            }
        } else{
            LOGGER.log(Level.FINEST, "La mossa non e' LATHEKIN TAGLIERINAMANUALE, passaggio responsabilita' all'handler successivo");
            return nextHandler.process(playerMove, remoteView, model);
        }
    }
}

