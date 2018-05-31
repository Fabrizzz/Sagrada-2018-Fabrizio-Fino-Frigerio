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

public class PinzaSgrossatriceHandler extends ToolHandler {

    @Override
    public void process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException

    {
        DraftPool draftPool;
        int draftPosition;
        boolean aumentaDiUno;
        Die die;

        //controlla fiches e aggiorna map

        if (playerMove.getTool() == Tool.PINZASGROSSATRICE) {
            if (!playerMove.getDraftPosition().isPresent() || !playerMove.getAumentaValoreDado().isPresent())
                throw new InvalidParameterException();
            try {
                draftPosition = playerMove.getDraftPosition().get();
                aumentaDiUno = playerMove.getAumentaValoreDado().get();
                draftPool = model.getDraftPool();
                die = draftPool.getDie(draftPosition);  //non dovrebbe lanciare eccezioni perchè ho già fatto il controllo nel first check

                if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) || (die.getNumber() == NumberEnum.ONE && !aumentaDiUno) || (die.getNumber() == NumberEnum.SIX && aumentaDiUno))
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                else {
                        if (aumentaDiUno)
                            die.setNumber(NumberEnum.getNumber(die.getNumber().getInt() + 1));
                        else
                            die.setNumber(NumberEnum.getNumber(die.getNumber().getInt() - 1));

                    completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                        nextHandler.process(playerMove, remoteView, model);
                }

            } catch (NoDieException e) {
                e.printStackTrace();
            }

        } else
            nextHandler.process(playerMove, remoteView, model);

    }
}
