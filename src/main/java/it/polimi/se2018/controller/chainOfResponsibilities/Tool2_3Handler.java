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

public class Tool2_3Handler extends ToolHandler {
    Tool toolname;

    public Tool2_3Handler(Tool toolname) {
        this.toolname = toolname;
        if (toolname != Tool.PENNELLOPEREGLOMISE && toolname != Tool.ALESATOREPERLAMINADIRAME){
            LOGGER.log(Level.SEVERE,"Errore parametri PENNELLOPEREGLOMISE ALESATOREPERLAMINADIRAME");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {
        int row;
        int column;
        int finalRow;
        int finalColumn;
        Die die;
        PlayerBoard board;

        if (playerMove.getTool() == toolname) {
            LOGGER.log(Level.FINE,"Elaborazione validita' mossa PENNELLOPEREGLOMISE ALESATOREPERLAMINADIRAME");

            Optional<Integer> rowO = playerMove.getRow();
            Optional<Integer> columnO =  playerMove.getColumn();
            Optional<Integer> finalRowO = playerMove.getFinalRow();
            Optional<Integer> finalColumnO =  playerMove.getFinalColumn();

            if (rowO.isPresent() && columnO.isPresent() && finalRowO.isPresent() && finalColumnO.isPresent()){
                board = model.getBoard(remoteView.getPlayer());
                row = rowO.get();
                column = columnO.get();
                finalColumn = finalColumnO.get();
                finalRow = finalRowO.get();

                if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) ||
                        !board.containsDie(row, column) || board.containsDie(finalRow, finalColumn)){
                    LOGGER.log(Level.INFO, "Il giocatore non puo' utilizzare PENNELLOPEREGLOMISE ALESATOREPERLAMINADIRAME 1");
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                } else try {
                    die = board.getDie(row, column);
                    board.removeDie(row, column);
                    if (!board.verifyNearCellsRestriction(die, finalRow, finalColumn) || !board.verifyPositionRestriction(finalRow, finalColumn) ||
                            (toolname == Tool.ALESATOREPERLAMINADIRAME && !board.verifyColorRestriction(die, finalRow, finalColumn)) || // il problma puo' manifestarsi anche qui ma non ho ancora testato
                            (toolname == Tool.PENNELLOPEREGLOMISE && !board.verifyNumberRestriction(die, finalRow, finalColumn))){ // Il problema e' qui, durante il controllo del numero se lo spostamento effettuato e' di una sola casella ortogonalmente alla vecchia posizione il controllo ritorna esito negativo perche' il dado nella nuova posizione viene confrontato con sestesso nella vecchia posizione dando errore
                        LOGGER.log(Level.INFO, "Il giocatore non puo' utilizzare PENNELLOPEREGLOMISE ALESATOREPERLAMINADIRAME 2");
                        board.setDie(die, row, column);
                        remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                    } else {

                        board.setDie(die, finalRow, finalColumn);
                        completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                        return true;
                    }

                } catch (NoDieException | AlredySetDie e) {
                    LOGGER.log(Level.SEVERE, "Dado non presente in PENNELLOPEREGLOMISE ALESATOREPERLAMINADIRAME");
                }

            } else {
                LOGGER.log(Level.SEVERE,"Errore parametri PENNELLOPEREGLOMISE ALESATOREPERLAMINADIRAME");
                throw new InvalidParameterException();
            }

        } else{
            LOGGER.log(Level.FINEST, "La mossa non e' PENNELLOPEREGLOMISE ALESATOREPERLAMINADIRAME, passaggio responsabilita' all'handler successivo");
            return nextHandler.process(playerMove, remoteView, model);
        }
        return false;
    }
}
