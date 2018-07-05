package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.Optional;
import java.util.logging.Level;

public class TaglierinaCircolareHandler extends ToolHandler {

    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        RoundTrack roundTrack;
        DraftPool draftPool;
        int draftPoolPosition;
        int roundTrackRound;
        int roundTrackPosition;

        if (playerMove.getTool() == Tool.TAGLIERINACIRCOLARE) {
            LOGGER.log(Level.FINE,"Elaborazione validita' mossa TAGLIERINACIRCOLARE");

            roundTrack = model.getRoundTrack();
            draftPool = model.getDraftPool();

            Optional<Integer> draftPoolPositionO = playerMove.getDraftPosition();
            Optional<Integer> roundTrackRoundO = playerMove.getRoundTrackRound();
            Optional<Integer> roundTrackPositionO = playerMove.getRoundTrackPosition();
            if (draftPoolPositionO.isPresent() && roundTrackRoundO.isPresent() && roundTrackPositionO.isPresent()) {
                draftPoolPosition = draftPoolPositionO.get();
                roundTrackRound = roundTrackRoundO.get();
                roundTrackPosition = roundTrackPositionO.get();
            }else{
                LOGGER.log(Level.SEVERE, "Errore parametri TAGLIERINACIRCOLARE");
                throw new InvalidParameterException();
            }

            if (draftPoolPosition < 0 || roundTrackRound < 0 || roundTrackPosition < 0 ||
                    draftPoolPosition >= model.getDraftPool().size() || roundTrackRound > model.getRound() ||
                    roundTrackPosition >= model.getRoundTrack().numberOfDice(roundTrackRound)){
                LOGGER.log(Level.INFO, "Errore parametri");
                remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
            }else {
                if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool())) {
                    LOGGER.log(Level.INFO, "Il giocatore non puo' utilizzare TAGLIERINACIRCOLARE");
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                } else try {
                    Die roundTrackDie = roundTrack.getDie(roundTrackRound, roundTrackPosition);
                    Die draftPoolDie = draftPool.getDie(draftPoolPosition);

                    roundTrack.removeDie(roundTrackRound, roundTrackPosition);
                    draftPool.removeDie(draftPoolDie);
                    roundTrack.addDie(roundTrackRound, draftPoolDie);
                    draftPool.addDie(roundTrackDie);
                    completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                    return true;
                } catch (NoDieException e) {
                    LOGGER.log(Level.SEVERE, "Dado non presente in TAGLIERINACIRCOLARE");
                }
            }
        } else {
            LOGGER.log(Level.FINEST, "La mossa non e' TAGLIERINACIRCOLARE, passaggio responsabilita' all'handler successivo");
            return nextHandler.process(playerMove, remoteView, model);
        }
        return false;
    }
}
