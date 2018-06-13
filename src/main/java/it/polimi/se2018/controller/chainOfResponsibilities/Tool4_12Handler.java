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

public class Tool4_12Handler extends ToolHandler {

    Tool tool;

    public Tool4_12Handler(Tool tool) {
        this.tool = tool;
        if (tool != Tool.LATHEKIN && tool != Tool.TAGLIERINAMANUALE)
            throw new IllegalArgumentException();
    }

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

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

            if (!playerMove.getRow().isPresent() || !playerMove.getColumn().isPresent() ||
                    !playerMove.getFinalRow().isPresent() || !playerMove.getFinalColumn().isPresent() ||
                    (!playerMove.getNextMove().isPresent() && tool == Tool.LATHEKIN))
                throw new InvalidParameterException();

            firstRow = playerMove.getRow().get();
            firstColumn = playerMove.getColumn().get();
            firstFinalColumn = playerMove.getFinalColumn().get();
            firstFinalRow = playerMove.getFinalRow().get();

            if (playerMove.getNextMove().isPresent()) {
                playerMove2 = playerMove.getNextMove().get();
                if (!playerMove2.getRow().isPresent() || !playerMove2.getColumn().isPresent() ||
                        !playerMove2.getFinalRow().isPresent() || !playerMove2.getFinalColumn().isPresent())
                    throw new InvalidParameterException();

                secondRow = playerMove2.getRow().get();
                secondColumn = playerMove2.getColumn().get();
                secondFinalColumn = playerMove2.getFinalColumn().get();
                secondFinalRow = playerMove2.getFinalRow().get();

                if (secondColumn < 0 || secondColumn > 4 || secondRow < 0 || secondRow > 3 ||
                        secondFinalColumn < 0 || secondFinalColumn > 4 || secondFinalRow < 0 || secondFinalRow > 3)
                    throw new InvalidParameterException();
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
                    nextHandler.process(playerMove, remoteView, model);

                } else
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));


            } catch (AlredySetDie alredySetDie) {
                alredySetDie.printStackTrace();
            } catch (NoDieException e) {
                e.printStackTrace();
            }
        } else
            nextHandler.process(playerMove, remoteView, model);

    }
}

