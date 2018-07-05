package it.polimi.se2018.view;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.utils.InputUtils;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUISwingProxy extends View {
    private GUISwing gameWindow;
    private static final Logger LOGGER = Logger.getLogger("Logger");

    public void gameWindow(GUISwing gameWindow){
        this.gameWindow = gameWindow;

    }

    @Override
    public  void connectionClosed() {
        gameWindow.printError("Connessione persa");
    }

    @Override
    public void update(Observable o, Object arg) {
        ServerMessage message = (ServerMessage) arg;
        switch (message.getMessageType()) {
            case ERROR:
                if(message.getErrorType().equals(ErrorType.ILLEGALMOVE)){
                    gameWindow.printError("Mossa inviata non valida, ripeti la scelta");
                }else{
                    gameWindow.printError("Non e' il tuo turno");
                }
                break;
            case INITIALCONFIGSERVER:
                gameWindow.setModelView(message.getModelView());
                break;
            case BOARDTOCHOOSE:
                gameWindow.chooseBoard(message.getBoards());
                break;
            case MODELVIEWUPDATE:
                LOGGER.log(Level.FINE,"ModelviewUpdate ricevuto");
                gameWindow.setModelView(new ModelView(gameWindow.getModelView(), message.getModelView()));
                break;
            case HASDISCONNECTED:
                gameWindow.printError("Il giocatore " +  message.getDisconnectedPlayer() + " si e' disconnesso");
                break;
            case HASRICONNECTED:
                gameWindow.printError("Il giocatore " +  message.getDisconnectedPlayer() + " si e' riconnesso");
                break;
            case ENDGAME:
                gameWindow.endGame(message.getScores());
                break;
            default:
                LOGGER.log(Level.WARNING,"Messaggio ricevuto di tipo non elaborabile");
                break;
        }
    }

    public void selectedBoard(Board board){
        setChanged();
        notifyObservers(new ClientMessage(board));
    }
}
