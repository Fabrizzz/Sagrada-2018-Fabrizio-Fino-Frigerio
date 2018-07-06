package it.polimi.se2018.view;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUISwingProxy extends View {
    private GUIMain gameWindow;
    private ModelView modelView;
    private static final Logger LOGGER = Logger.getLogger("Logger");
    private Long localID;

    public void gameWindow(GUIMain gameWindow, Long localID){
        this.gameWindow = gameWindow;
        this.localID = localID;
    }

    @Override
    public  void connectionClosed() {
        gameWindow.printError("Connessione persa");
    }

    @Override
    public void elaborateMessage(ServerMessage message) {
        switch (message.getMessageType()) {
            case ERROR:
                if(message.getErrorType().equals(ErrorType.ILLEGALMOVE)){
                    gameWindow.printError("Mossa inviata non valida, ripeti la scelta");
                }else if(message.getErrorType().equals(ErrorType.NOTYOURTURN)){
                    gameWindow.printError("Non e' il tuo turno");
                }else if(message.getErrorType().equals(ErrorType.CONNECTIONREFUSED)){
                    gameWindow.printError("Errore di connessione: non puoi avere lo stesso nickname di un altro giocatore connesso");
                    System.exit(0);
                }
                break;
            case INITIALCONFIGSERVER:
                modelView = message.getModelView();
                gameWindow.setModelView(modelView);
                break;
            case BOARDTOCHOOSE:
                gameWindow.chooseBoard(message.getBoards());
                break;
            case MODELVIEWUPDATE:
                LOGGER.log(Level.FINE,"ModelviewUpdate ricevuto");
                modelView.update(message.getUpdate());
                gameWindow.setModelView(modelView);
                break;
            case HASDISCONNECTED:
                gameWindow.printError("Il giocatore " +  message.getDisconnectedPlayer() + " si e' disconnesso");
                break;
            case HASRICONNECTED:
                try{
                    if(!message.getDisconnectedPlayer().equals(modelView.getPlayer(localID).getNick()))
                        gameWindow.printError("Il giocatore " +  message.getDisconnectedPlayer() + " si e' riconnesso");
                }catch (Exception e){}
                break;
            case ENDGAME:
                gameWindow.endGame(message);
                break;
            default:
                LOGGER.log(Level.WARNING,"Messaggio ricevuto di tipo non elaborabile");
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        ServerMessage message = (ServerMessage) arg;
        elaborateMessage(message);

    }

    public void sendMessage(ClientMessage clientMessage){
        setChanged();
        notifyObservers(clientMessage);
    }

    public void selectedBoard(Board board){
        setChanged();
        notifyObservers(new ClientMessage(board));
    }
}
