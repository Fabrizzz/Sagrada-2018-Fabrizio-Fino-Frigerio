package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
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

import java.util.logging.Level;

public class PennelloPerPastaSaldaHandler extends ToolHandler {


    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {
        DraftPool draftPool;
        NumberEnum newValue;
        PlayerBoard board;
        int row, column;
        int draftPoolPosition;

        if (playerMove.getTool() == Tool.PENNELLOPERPASTASALDA) {
            LOGGER.log(Level.FINE,"Elaborazione validita' PENNELLOPERPASTASALDA");
            if (!playerMove.getNewDiceValue().isPresent() || !playerMove.getDraftPosition().isPresent()){
                LOGGER.log(Level.SEVERE,"Errore parametri PENNELLOPERPASTASALDA");
                throw new InvalidParameterException();
            }

            try {

                draftPool = model.getDraftPool();
                draftPoolPosition = playerMove.getDraftPosition().get();
                newValue = playerMove.getNewDiceValue().get();
                Die die = draftPool.getDie(draftPoolPosition);
                if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool())){
                    LOGGER.log(Level.INFO,"Il giocatore non puo' utilizzare PENNELLOPERPASTASALDA");
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                }

                else {
                    //die.setNumber(newValue);
                    //completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                    board = model.getBoard(remoteView.getPlayer());

                    if (playerMove.getRow().isPresent() && playerMove.getColumn().isPresent()) {
                        row = playerMove.getRow().get();
                        column = playerMove.getColumn().get();
                        NumberEnum num = die.getNumber();
                        die.setNumber(newValue);


                        if (board.containsDie(row, column) ||
                                !board.verifyInitialPositionRestriction(row, column) ||
                                !board.verifyNumberRestriction(die, row, column) ||
                                !board.verifyColorRestriction(die, row, column) ||
                                !board.verifyNearCellsRestriction(die, row, column) ||
                                !board.verifyPositionRestriction(row, column)) {
                            LOGGER.log(Level.INFO,"Il giocatore non puo' utilizzare PENNELLOPERPASTASALDA");
                            remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                            die.setNumber(num);
                        } else {
                            board.setDie(die, row, column);
                            draftPool.removeDie(die);
                            completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                            nextHandler.process(playerMove, remoteView, model);
                        }
                    } else {
                        NumberEnum num = die.getNumber();
                        die.setNumber(newValue);
                        boolean check = true;
                        for (int r = 0; r < 4 && check; r++) {
                            for (int c = 0; c < 5 && check; c++) {
                                if (board.verifyInitialPositionRestriction(r, c) &&
                                        board.verifyNumberRestriction(die, r, c) &&
                                        board.verifyColorRestriction(die, r, c) &&
                                        board.verifyNearCellsRestriction(die, r, c) &&
                                        board.verifyPositionRestriction(r, c))
                                    check = false;
                            }
                        }
                        if (check) {
                            die.setNumber(newValue);
                            completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                            nextHandler.process(playerMove, remoteView, model);
                        } else {
                            die.setNumber(num);
                            LOGGER.log(Level.INFO,"Il giocatore non puo' utilizzare PENNELLOPERPASTASALDA");
                            remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                        }
                    }
                    //nextHandler.process(playerMove, remoteView, model);
                }

            } catch (NoDieException e) {
                LOGGER.log(Level.SEVERE, "Dado non presente in PENNELLOPERPASTASALDA");
                e.printStackTrace();
            } catch (AlredySetDie alredySetDie) {
                LOGGER.log(Level.SEVERE, "Dado gia' presente in PENNELLOPERPASTASALDA");
                alredySetDie.printStackTrace();
            }
        } else{
            LOGGER.log(Level.FINEST,"La mossa non e' PENNELLOPERPASTASALDA, passaggio responsabilita' all'handler successivo");
            nextHandler.process(playerMove, remoteView, model);
        }


    }
}
