package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.ServerMessage;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;

public class Tool2_3Handler extends ToolHandler {
    Tool toolname;

    public Tool2_3Handler(Tool toolname) {
        this.toolname = toolname;
        if (toolname != Tool.PENNELLOPEREGLOMISE && toolname != Tool.ALESATOREPERLAMINADIRAME)
            throw new IllegalArgumentException();
    }

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {
        int row;
        int column;
        int finalRow;
        int finalColumn;
        Die die;
        PlayerBoard board;

        if (playerMove.getTool() == toolname) {
            if (!playerMove.getRow().isPresent() || !playerMove.getColumn().isPresent() ||
                    !playerMove.getFinalColumn().isPresent() || !playerMove.getFinalRow().isPresent())
                throw new InvalidParameterException();
            else {
                board = model.getBoard(remoteView.getPlayer());
                row = playerMove.getRow().get();
                column = playerMove.getColumn().get();
                finalColumn = playerMove.getFinalColumn().get();
                finalRow = playerMove.getFinalRow().get();


                if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) ||
                        !board.containsDie(row, column) || board.containsDie(finalRow, finalColumn))
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                else try {
                    die = board.getDie(row, column);
                    if (!board.verifyNearCellsRestriction(die, finalRow, finalColumn) || !board.verifyPositionRestriction(finalRow, finalColumn) ||
                            (toolname == Tool.ALESATOREPERLAMINADIRAME && !board.verifyColorRestriction(die, finalRow, finalColumn)) ||
                            (toolname == Tool.PENNELLOPEREGLOMISE && !board.verifyNumberRestriction(die, finalRow, finalColumn)))
                        remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                    else {

                            board.removeDie(row, column);
                            board.setDie(die, finalRow, finalColumn);
                        completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                            nextHandler.process(playerMove, remoteView, model);
                    }

                } catch (NoDieException | AlredySetDie e) {
                    e.printStackTrace();
                }

            }

        } else
            nextHandler.process(playerMove, remoteView, model);
    }
}
