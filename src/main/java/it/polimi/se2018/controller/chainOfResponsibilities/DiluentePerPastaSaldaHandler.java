package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.DiceBag;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

public class DiluentePerPastaSaldaHandler extends ToolHandler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {
        DraftPool draftPool;
        NumberEnum newValue;
        PlayerBoard board;
        DiceBag diceBag;
        int row, column;
        int draftPoolPosition;
        Die dieToRemove;
        Die dieToGet;

        if (playerMove.getTool() == Tool.DILUENTEPERPASTASALDA) {
            if (!playerMove.getNewDiceValue().isPresent() || !playerMove.getDraftPosition().isPresent())
                throw new InvalidParameterException();
            try {

                draftPool = model.getDraftPool();
                draftPoolPosition = playerMove.getDraftPosition().get();
                newValue = playerMove.getNewDiceValue().get();
                diceBag = model.getDiceBag();
                dieToGet = diceBag.getFirst();
                dieToRemove = draftPool.getDie(draftPoolPosition);
                if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()))
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                else {
                    board = model.getBoard(remoteView.getPlayer());

                    if (playerMove.getRow().isPresent() && playerMove.getColumn().isPresent()) {
                        row = playerMove.getRow().get();
                        column = playerMove.getColumn().get();
                        NumberEnum num = dieToGet.getNumber();
                        dieToGet.setNumber(newValue);


                        if (board.containsDie(row, column) ||
                                !board.verifyInitialPositionRestriction(row, column) ||
                                !board.verifyNumberRestriction(dieToGet, row, column) ||
                                !board.verifyColorRestriction(dieToGet, row, column) ||
                                !board.verifyNearCellsRestriction(dieToGet, row, column) ||
                                !board.verifyPositionRestriction(row, column)) {
                            dieToGet.setNumber(num);
                            remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                        } else {
                            diceBag.takeDie();
                            board.setDie(dieToGet, row, column);
                            draftPool.removeDie(dieToRemove);
                            diceBag.addDie(dieToRemove);
                            completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                            nextHandler.process(playerMove, remoteView, model);
                        }
                    } else {
                        boolean check = true;
                        for (int r = 0; r < 4 && check; r++) {
                            for (int c = 0; c < 5 && check; c++) {
                                if (board.verifyInitialPositionRestriction(r, c) &&
                                        board.verifyNumberRestriction(dieToGet, r, c) &&
                                        board.verifyColorRestriction(dieToGet, r, c) &&
                                        board.verifyNearCellsRestriction(dieToGet, r, c) &&
                                        board.verifyPositionRestriction(r, c))
                                    check = false;
                            }
                        }
                        if (check) {
                            dieToGet.setNumber(newValue);
                            diceBag.takeDie();
                            draftPool.removeDie(dieToRemove);
                            draftPool.addDie(dieToGet);
                            completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                            nextHandler.process(playerMove, remoteView, model);
                        } else {
                            remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                        }
                    }
                    //nextHandler.process(playerMove, remoteView, model);
                }

            } catch (NoDieException e) {
                e.printStackTrace();
            } catch (AlredySetDie alredySetDie) {
                alredySetDie.printStackTrace();
            }
        } else
            nextHandler.process(playerMove, remoteView, model);

    }
}
