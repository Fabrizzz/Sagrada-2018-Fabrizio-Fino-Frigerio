package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.controller.RemoteView;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.InvalidParameterException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.logging.Level;

public class PinzaSgrossatriceHandler extends ToolHandler {

    @Override
    public boolean process(PlayerMove playerMove, RemoteView remoteView, Model model) throws InvalidParameterException

    {
        DraftPool draftPool;
        int draftPosition;
        boolean aumentaDiUno;
        Die die;

        //controlla fiches e aggiorna map

        if (playerMove.getTool() == Tool.PINZASGROSSATRICE) {
            LOGGER.log(Level.FINE,"Elaborazione validita' mossa PINZASGROSSATRICE");
            if (!playerMove.getDraftPosition().isPresent() || !playerMove.getAumentaValoreDado().isPresent()){
                LOGGER.log(Level.SEVERE,"Errore parametri PINZASGROSSATRICE");
                throw new InvalidParameterException();
            }

            try {
                draftPosition = playerMove.getDraftPosition().orElse(0);
                aumentaDiUno = playerMove.getAumentaValoreDado().orElse(false);
                draftPool = model.getDraftPool();
                die = draftPool.getDie(draftPosition);  //non dovrebbe lanciare eccezioni perchè ho già fatto il controllo nel first check

                if (cantUseTool(remoteView.getPlayer(), model, playerMove.getTool()) || (die.getNumber() == NumberEnum.ONE && !aumentaDiUno) || (die.getNumber() == NumberEnum.SIX && aumentaDiUno)){
                    LOGGER.log(Level.INFO,"Il giocatore non puo' utilizzare PINZASGROSSATRICE");
                    remoteView.sendBack(new ServerMessage(ErrorType.ILLEGALMOVE));
                }else {
                        if (aumentaDiUno)
                            die.setNumber(NumberEnum.getNumber(die.getNumber().getInt() + 1));
                        else
                            die.setNumber(NumberEnum.getNumber(die.getNumber().getInt() - 1));

                    completeTool(remoteView.getPlayer(), model, playerMove.getTool());
                    return true;
                }

            } catch (NoDieException e) {
                LOGGER.log(Level.SEVERE, "Dado non presente in PINZASGROSSATRICE");
            }

        } else{
            LOGGER.log(Level.FINEST,"La mossa non e' PINZASGROSSATRICE, passaggio responsabilita' all'handler successivo");
            return nextHandler.process(playerMove, remoteView, model);
        }
        return false;

    }
}
