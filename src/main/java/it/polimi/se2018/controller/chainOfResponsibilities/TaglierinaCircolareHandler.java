package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.RoundTrack;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.ServerMessage;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;

public class TaglierinaCircolareHandler extends ToolHandler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException {

        RoundTrack roundTrack;
        DraftPool draftPool;
        int draftPoolPosition;
        int roundTrackRound;
        int roundTrackPosition;

        if (playerMove.getTool() == Tool.TAGLIERINACIRCOLARE) {
            if (!playerMove.getRoundTrackPosition().isPresent() || !playerMove.getRoundTrackRound().isPresent() ||
                    !playerMove.getDraftPosition().isPresent())
                throw new InvalidParameterException();

            roundTrack = model.getRoundTrack();
            draftPool = model.getDraftPool();
            draftPoolPosition = playerMove.getDraftPosition().get();
            roundTrackRound = playerMove.getRoundTrackRound().get();
            roundTrackPosition = playerMove.getRoundTrackPosition().get();


            if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) || roundTrackPosition >= roundTrack.numberOfDice(roundTrackRound))
                remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
            else try {
                Die roundTrackDie = roundTrack.getDie(roundTrackRound, roundTrackPosition);
                Die draftPoolDie = draftPool.getDie(draftPoolPosition);

                roundTrack.removeDie(roundTrackRound, roundTrackPosition);
                draftPool.removeDie(draftPoolDie);
                roundTrack.addDie(roundTrackRound, draftPoolDie);
                draftPool.addDie(roundTrackDie);
                completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                nextHandler.process(playerMove, remoteView, model);
            } catch (NoDieException e) {
                e.printStackTrace();
            }
        } else
            nextHandler.process(playerMove, remoteView, model);
    }
}
