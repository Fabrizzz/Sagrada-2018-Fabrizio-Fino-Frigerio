package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlreadySetDie;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.Optional;
import java.util.logging.Level;

public class PennelloPerPastaSaldaHandler extends ToolHandler {


    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {
        DraftPool draftPool;
        NumberEnum newValue;
        PlayerBoard board;
        int row;
        int column;
        int draftPoolPosition;

        if (playerMove.getTool() == Tool.PENNELLOPERPASTASALDA) {
            LOGGER.log(Level.FINE,"Elaborazione validita' PENNELLOPERPASTASALDA");

            try {
                draftPool = model.getDraftPool();
                Optional<Integer> draftPoolPositionO = playerMove.getDraftPosition();
                Optional<NumberEnum> newValueO =  playerMove.getNewDiceValue();
                if (newValueO.isPresent() && draftPoolPositionO.isPresent()) {
                    draftPoolPosition = draftPoolPositionO.get();
                    newValue = newValueO.get();
                }else {
                    LOGGER.log(Level.SEVERE,"Errore parametri PENNELLOPERPASTASALDA");
                    throw new InvalidParameterException();
                }

                Die die = draftPool.getDie(draftPoolPosition);
                if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) || model.hasUsedNormalMove()) {
                    LOGGER.log(Level.INFO,"Il giocatore non puo' utilizzare PENNELLOPERPASTASALDA 1");
                    remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                }

                else {
                    //die.setNumber(newValue);
                    //completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                    board = model.getBoard(remoteView.getPlayer());
                    Optional<Integer> rowO = playerMove.getRow();
                    Optional<Integer> colO = playerMove.getColumn();
                    if (rowO.isPresent() && colO.isPresent()) {
                        row = rowO.get();
                        column = colO.get();
                        NumberEnum num = die.getNumber();
                        die.setNumber(newValue);

                        if ((board.isEmpty() && !board.verifyInitialPositionRestriction(row, column)) ||
                                (!board.isEmpty() && (
                                board.containsDie(row, column) ||
                                !board.verifyColorRestriction(die, row, column) ||
                                !board.verifyNumberRestriction(die, row, column) ||
                                !board.verifyNearCellsRestriction(die, row, column) ||
                                !board.verifyPositionRestriction(row, column))))  {
                            LOGGER.log(Level.INFO,"Il giocatore non puo' utilizzare PENNELLOPERPASTASALDA 2");
                            die.setNumber(num);
                            remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                            return false;
                        } else {
                            board.setDie(die, row, column);
                            draftPool.removeDie(die);
                            completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                            return true;
                        }
                    } else {
                        NumberEnum num = die.getNumber();
                        die.setNumber(newValue);
                        boolean check = true;
                        if(board.isEmpty()){
                            check = false;
                            LOGGER.log(Level.FINE,"Board vuota");
                        }else{
                            LOGGER.log(Level.FINE,"Controllo mosse possibili");
                            for (int r = 0; r < 4 && check; r++) {
                                for (int c = 0; c < 5 && check; c++) {
                                    if ((board.verifyInitialPositionRestriction(r, c) && board.isEmpty()) ||
                                            (!board.containsDie(r, c) && board.verifyNumberRestriction(die, r, c) &&
                                                    board.verifyColorRestriction(die, r, c) &&
                                                    board.verifyNearCellsRestriction(die, r, c) &&
                                                    board.verifyPositionRestriction(r, c)))
                                        check = false;
                                }
                            }
                        }
                        if (check) {
                            completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                            return true;
                        } else {
                            die.setNumber(num);
                            LOGGER.log(Level.INFO,"Il giocatore non puo' utilizzare PENNELLOPERPASTASALDA 3");
                            remoteView.elaborateMessage(new ServerMessage(ErrorType.ILLEGALMOVE));
                            return false;
                        }
                    }
                    //nextHandler.process(playerMove, remoteView, model);
                }

            } catch (NoDieException e) {
                LOGGER.log(Level.SEVERE, "Dado non presente in PENNELLOPERPASTASALDA");
            } catch (AlreadySetDie alreadySetDie) {
                LOGGER.log(Level.SEVERE, "Dado gia' presente in PENNELLOPERPASTASALDA");
            }
        } else{
            LOGGER.log(Level.FINEST,"La mossa non e' PENNELLOPERPASTASALDA, passaggio responsabilita' all'handler successivo");
            return nextHandler.process(playerMove, remoteView, model);
        }
        return false;

    }
}
