package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.ServerMessage;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;

public class PinzaSgrossatriceHandler extends Handler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException

    {
        DraftPool draftPool;
        int draftPosition;
        boolean aumentaDiUno;
        boolean alreadyUsed;
        Die die;

        //controlla fiches e aggiorna map

        if (playerMove.getTool() == Tool.PINZASGROSSATRICE) {
            if (!playerMove.getDraftPosition().isPresent() || !playerMove.getAumentaValoreDado().isPresent())
                throw new InvalidParameterException();
            else try {
                draftPosition = playerMove.getDraftPosition().get();
                aumentaDiUno = playerMove.getAumentaValoreDado().get();
                draftPool = model.getDraftPool();
                die = draftPool.getDie(draftPosition);  //non dovrebbe lanciare eccezioni perchè ho già fatto il controllo nel first check
                alreadyUsed = model.getTools().get(playerMove.getTool());


                if (model.hasUsedTool() || (die.getNumber() == NumberEnum.ONE && !aumentaDiUno) || (die.getNumber() == NumberEnum.SIX && aumentaDiUno) ||
                        (alreadyUsed && remoteView.getPlayer().getFavorTokens() < 2) || (!alreadyUsed && remoteView.getPlayer().getFavorTokens() < 1))
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                else {
                    if (!model.isTimerScaduto()) {
                        if (aumentaDiUno)
                            die.setNumber(NumberEnum.getNumber(die.getNumber().getInt() + 1));
                        else
                            die.setNumber(NumberEnum.getNumber(die.getNumber().getInt() - 1));

                        remoteView.getPlayer().setFavorTokens(remoteView.getPlayer().getFavorTokens() - (alreadyUsed ? 2 : 1));
                        model.getTools().put(playerMove.getTool(), true);
                        model.setUsedTool(true);
                        nextHandler.process(playerMove, remoteView, model);
                    } else
                        remoteView.sendBack(new ServerMessage(ErrorType.TIMERSCADUTO));
                }

            } catch (NoDieException e) {
                e.printStackTrace();
            }

        } else
            nextHandler.process(playerMove, remoteView, model);

    }
}
