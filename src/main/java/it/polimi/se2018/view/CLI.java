package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.server.Server;
import it.polimi.se2018.utils.InputUtils;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.io.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Command Line Interface
 * @author Alessio
 */
public class CLI extends View{
    private static final String ANSI_RESET = "\u001B[0m";
    private Map<Color,String> colorMap = new EnumMap<Color, String>(Color.class);
    private ModelView modelView;
    private Long localID;
    private static final Logger LOGGER = Logger.getLogger("Logger");

    /**
     * Constructor
     */
    public CLI(){
        colorMap.put(Color.BLUE,"\u001B[34m");
        colorMap.put(Color.RED,"\u001B[31m");
        colorMap.put(Color.GREEN, "\u001B[32m");
        colorMap.put(Color.YELLOW,"\u001B[33m");
        colorMap.put(Color.PURPLE, "\u001B[35m");
    }
    
    private void println(String string){
        System.out.println(string);
    }
    
    private void print(String string){
        System.out.print(string);
    }
    
    /**
     * Show a playerboard to the player
     * @param playerBoard playerboard to show
     */
    private void showPlayerBoard(PlayerBoard playerBoard){
        println("\nPlancia dei dadi");
        println("__________|| Riga");

        for(int j = 0; j < 4; j ++){
            for(int i = 0; i < 5; i++){
                try{
                    print("|" + colorMap.get(playerBoard.getDie(j,i).getColor()) + playerBoard.getDie(j,i).getNumber().getInt() + ANSI_RESET);
                }catch(NoDieException e) {
                    print("| ");
                }
            }
            println("|| "+(j+1));
        }
        println("‾‾‾‾‾‾‾‾‾‾");
        println("|1|2|3|4|5|| Colonna\n");



        showBoard(playerBoard);
    }

    /**
     * Show the  board containing the restriction
     * @param playerBoard player board to show
     */
    private void showBoard(PlayerBoard playerBoard){
        println("Plancia delle restrizioni");
        println("__________|| Riga");

        for(int j = 0; j < 4; j ++){
            for(int i = 0; i < 5; i++){

                if(playerBoard.getRestriction(j,i).isNumberRestriction()){
                    print("|" + (((NumberRestriction) (playerBoard.getRestriction(j,i))).getNumber()).getInt());
                }else if(playerBoard.getRestriction(j,i).isColorRestriction()){
                    print("|" + colorMap.get(((ColorRestriction) playerBoard.getRestriction(j,i)).getColor()) + "x" + ANSI_RESET);
                }else{
                    print("| ");
                }

            }
            println("|| "+(j+1));
        }
        println("‾‾‾‾‾‾‾‾‾‾");
        println("|1|2|3|4|5|| Colonna\n\n");
    }

    /**
     * Show the draftpool to the player
     */
    private void showDraftPool(){
        println("Riserva dei dadi");

        print("Posizione:");
        for(int i = 0; i <  modelView.DraftPoolSize();i++){
            print("|"+(i+1));
        }
        println("|");

        print("Dadi:     ");
        for(int i = 0; i < modelView.DraftPoolSize(); i++){
            try{
                print("|" + colorMap.get(modelView.getDraftPoolDie(i).getColor()) + modelView.getDraftPoolDie(i).getNumber().getInt() + ANSI_RESET);
            }catch(NoDieException e){
                print("| ");
            }
        }
        println("|\n\n");
    }

    /**
     * Show the toolcards to the player
     */
    private void showToolCards(){
        println("Carte strumento:");
        for (int i = 0; i < modelView.getTools().size(); i++) {
            print("Nome: ");
            println(modelView.getTools().keySet().toArray()[i].toString());
            print("Descrizione: ");
            println(((Tool) modelView.getTools().keySet().toArray()[i]).getDescription());
            println("Usato: "+modelView.getTools().get(modelView.getTools().keySet().toArray()[i]) + "\n\n");
        }
    }

    /**
     * Show the public objectives cards
     */
    private void showPublicObjectives(){
        println("Carte obiettivo pubbliche:");
        for(int i = 0; i < modelView.getPublicObjective().size(); i ++){
            println("Numero " + (i+1));
            println(modelView.getPublicObjective().get(i).getObjectiveName() + "\n\n");
        }
    }

    /**
     * Show the player private objective
     */
    private void showPrivateObjective(){
        println("Colore obiettivo privato: \n\n" /*+ modelView.getPlayer(localID).*/);//TODO
    }

    /**
     * Show the round track to the player
     */
    private void showRoundTrack(){
        println("Tracciato dei dadi:");
        for(int i = 0; i < modelView.getRound(); i ++){
            println("Round " + (i+1));
            for(int j = 0; j < modelView.getRoundTrack().numberOfDice(i); j ++){
                try{
                    println("|" + modelView.getRoundTrackDie(i,j));
                }catch (NoDieException e){
                    LOGGER.log(Level.FINEST,"Dado non presente");
                }
            }
            println("|||\n\n");
        }
    }

    private int[] chooseRoundTrackDie(){
        int[] position = new int[2];
        if(modelView.getRoundTrack().numberOfDice(modelView.getRound()) == 0){
            println("Errore, il tracciato dei dadi e' vuoto");
            position[0] = -1;
            position[1] = -1;
        }
        showRoundTrack();
        position[0] = modelView.getRound();

        println("Inserisci la posizione del dado: ");
        do {
            position[1] = InputUtils.getInt();
        }while (position[1] > 0 && position[1] < modelView.getRoundTrack().numberOfDice(position[0] - 1));

        position[1] --;

        return position;
    }

    /**
     * Let the player choose a die from the draftpool
     * @return the index of the chosen die
     */
    private int chooseDraftpoolDie(){
        println("Scegli il dado dalla riserva");
        showDraftPool();
        println("Inserisci la posizione del dado scelto:");
        int i = InputUtils.getInt();
        while (i < 1 || i > modelView.DraftPoolSize()) {
            println("Errore, inserisci una posizione corretta");
            i = InputUtils.getInt();
        }
        return i - 1;

    }

    /**
     * Let the player choose a cell in his playerboard
     * @return  the array containing the row and column coordinates to identify the cell
     */
    private int[] chooseBoardCellWithDie(){
        int[] position = new int[2];
        boolean repeat = true;

        if(modelView.getBoard(modelView.getPlayer(localID)).isEmpty()){
            println("La plancia e' vuota, non puoi eseguire questa mossa");
            position[0] = -1;
            position[1] = -1;
            LOGGER.log(Level.FINE,"La plancia e' vuota");
            return position;
        }

        do {
            showPlayerBoard(modelView.getBoard(modelView.getPlayer(localID)));

            position[0] = chooseRow();
            position[1] = chooseColumn();

            if(modelView.getBoard(modelView.getPlayer(localID)).containsDie(position[0],position[1])){
                repeat = false;
            }else{
                repeat = true;
                println("Errore: nessun dado presente in posizione riga: " + position[0] + ", colonna: " + position[1] + ". Ripetere la scelta");
            }
        }while(repeat);

        return position;
    }

    private int chooseRow(){
        int row = 0;
        println("Inserisci l'indice di riga: ");
        row = InputUtils.getInt();
        while (row < 1 || row > 4) {
            println("Errore, inserisci un indice corretto");
            row = InputUtils.getInt();
        }
        return row - 1;
    }

    private int chooseColumn(){
        int column = 0;
        println("Inserisci l'indice di colonna: ");
        column = InputUtils.getInt();
        while (column < 1 || column > 5) {
            println("Errore, inserisci un indice corretto");
            column = InputUtils.getInt();
        }

        return column - 1;
    }

    private boolean boardIsFull(PlayerBoard board){
        for(int k = 0; k < 5; k++){
            for(int h = 0; h < 4; h++){
                if(!board.containsDie(h,k)){
                    return false;
                }
            }
        }
        return true;
    }
    private int[] chooseBoardCellWithoutDie(){
        int[] position = new int[2];
        boolean repeat = true;

        if(boardIsFull(modelView.getBoard(modelView.getPlayer(localID)))){
            println("Errore, la board e' piena non puoi eseguire questa mossa");
            position[0] = -1;
            position[1] = -1;
            LOGGER.log(Level.FINE,"La plancia e' piena");
            return position;
        }else {
            do {
                showPlayerBoard(modelView.getBoard(modelView.getPlayer(localID)));

                position[0] = chooseRow();

                position[1] = chooseColumn();


                if (!modelView.getBoard(modelView.getPlayer(localID)).containsDie(position[0], position[1])) {
                    repeat = false;
                } else {
                    repeat = true;
                    println("Errore: dado presente in posizione riga: " + position[0] + ", colonna: " + position[1] + ". Ripetere la scelta");
                }
            } while (repeat);

            return position;
        }
    }

    /**
     * Execute an action
     * @param tool type of action to be executed
     */
    public void move(Tool tool){
        switch (tool) {
            case MOSSASTANDARD:
                normalSugheroMove(tool);
                break;
            case RIGAINSUGHERO:
                normalSugheroMove(tool);
                break;
            case SKIPTURN:
                skipMartellettoTenagliaMove(tool);
                break;
            case MARTELLETTO:
                skipMartellettoTenagliaMove(tool);
                break;
            case TENAGLIAAROTELLE:
                skipMartellettoTenagliaMove(tool);
                break;
            case PINZASGROSSATRICE:
                sgrossatriceMove();
                break;
            case PENNELLOPEREGLOMISE:
                pennelloAlesatoreLeathekinManualeMove(tool);
                break;
            case ALESATOREPERLAMINADIRAME:
                pennelloAlesatoreLeathekinManualeMove(tool);
                break;
            case TAGLIERINAMANUALE:
                pennelloAlesatoreLeathekinManualeMove(tool);
                break;
            case TAGLIERINACIRCOLARE:
                taglierinaCircolareMove();
                break;
            case PENNELLOPERPASTASALDA:
                pennelloPastaSaldaMove();
                break;
            case DILUENTEPERPASTASALDA:
                diluentePerPastaSaldaMove();
                break;
            case TAMPONEDIAMANTATO:
                tamponeDiamantato();
                break;
            default:
                break;
        }
    }

    /**
     * Execute a normal move or a riga in sughero action
     * @param tool tool to use
     */
    public void normalSugheroMove(Tool tool){
        int i = chooseDraftpoolDie();

        println("Scegli dove piazzare il dado");
        int[] position = chooseBoardCellWithoutDie();

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], i));
        setChanged();
        notifyObservers(clientMessage);
        println("Mossa inviata");
    }

    /**
     * Execute one of the following actions: skip turn, martelletto tool, tenaglia a rotelle tool
     * @param tool tool to use
     */
    private void skipMartellettoTenagliaMove(Tool tool){
        if ((tool == Tool.TENAGLIAAROTELLE && !modelView.isFirstTurn()) || (tool == Tool.MARTELLETTO && (modelView.isFirstTurn() || modelView.isNormalMove()))) {
            println("Errore: Non puoi eseguire questa mossa");
            chooseMove();
        } else {
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool));
            setChanged();
            notifyObservers(clientMessage);
            println("Mossa inviata");
            if (tool.equals(Tool.TENAGLIAAROTELLE)) {
                println("Esegui il primo piazzamento");
                normalSugheroMove(Tool.MOSSASTANDARD);
                clientMessage = new ClientMessage(new PlayerMove(Tool.TENAGLIAAROTELLE));
                setChanged();
                notifyObservers(clientMessage);
                println("Esegui il secondo piazzamento");
                normalSugheroMove(Tool.MOSSASTANDARD);
            }
        }
    }

    /**
     * Use the pinza sgrossatrice tool
     */
    private void sgrossatriceMove(){
       int position = chooseDraftpoolDie();
       try {
            println("Valore del dado selezionato: " + modelView.getDraftPoolDie(position).getNumber().getInt());
            int scelta = 3;
            while(scelta != 0 && scelta != 1){
                println("Inserisci 0 per diminuire il valore del dado selezionato, 1 per aumentare il valore del dado");
                scelta = InputUtils.getInt();
            }

            Boolean aumento = (scelta != 0);

            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.PINZASGROSSATRICE,position,aumento));
            setChanged();
            notifyObservers(clientMessage);
            println("Mossa inviata");

       }catch(NoDieException e){
           LOGGER.log(Level.FINEST, "Nessun dado presente");
           println("Errore, dado non presente nel draftpool, mossa annullata");
           chooseMove();
       }

    }

    /**
     * Use one of the following tools: pennello per eglomise, alesatore per lamina di rame,lathekin, taglierina manuale
     * @param tool tool to use
     */
    private void pennelloAlesatoreLeathekinManualeMove(Tool tool){
        boolean secondaMossa = false;
        println("Scegli il primo dado da muovere");
        int[] position = chooseBoardCellWithDie();
        if(position[0] == -1){
            chooseMove();
            return;
        }

        println("Scegli dove piazzare primo il dado");
        int[] newPosition = chooseBoardCellWithoutDie();
        if(newPosition[0] == -1){
            chooseMove();
            return;
        }

        if(tool == Tool.LATHEKIN){
            secondaMossa = true;
        }else if(tool == Tool.TAGLIERINAMANUALE){
            println("Vuoi scegliere un secondo dado da muovere? 0 no, 1 si");
            int scelta = 2;
            do{
                scelta = InputUtils.getInt();
            }while(scelta != 0 && scelta != 1);

            secondaMossa = (scelta == 1);
        }

        ClientMessage clientMessage;
        if(secondaMossa){
            println("Scegli il secondo dado da muovere");
            int[] position2 = chooseBoardCellWithDie();
            if(position2[0] == -1){
                chooseMove();
                return;
            }

            println("Scegli dove piazzare il secondo dado");
            int[] newPosition2 = chooseBoardCellWithoutDie();
            if(newPosition2[0] == -1){
                chooseMove();
                return;
            }
            clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], newPosition[0], newPosition[1],new PlayerMove(tool, position2[0], position2[1], newPosition2[0], newPosition2[1])));
        }else{
            clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], newPosition[0], newPosition[1]));
        }

        setChanged();
        notifyObservers(clientMessage);
        println("Mossa inviata");
    }

    /**
     * Use one the taglierina circolare tool
     */
    private void taglierinaCircolareMove(){
        int i = chooseDraftpoolDie();
        int[] roundPosition = chooseRoundTrackDie();
        if(roundPosition[0] == -1){
            chooseMove();
        }else {
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(i, roundPosition[0], roundPosition[1], Tool.TAGLIERINACIRCOLARE));
            setChanged();
            notifyObservers(clientMessage);
            println("Mossa inviata");
        }
    }

    /**
     * Use the pennello per pasta salda tool
     */
    private void pennelloPastaSaldaMove(){
        int i = chooseDraftpoolDie();
        NumberEnum newNum = NumberEnum.values()[(new Random()).nextInt(NumberEnum.values().length)];
        println("Il nuovo valore del dado e': " + newNum.getInt());
        int[] position = chooseBoardCellWithoutDie();
        if(position[0] == -1){
            chooseMove();
            return;
        }

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(position[0],position[1],i,newNum,Tool.PENNELLOPERPASTASALDA));
        setChanged();
        notifyObservers(clientMessage);
        println("Mossa inviata");
    }

    /**
     * Use the diluente per pasta salda tool
     */
    private void diluentePerPastaSaldaMove(){
        int i = chooseDraftpoolDie();
        try {
            modelView.getDiceBag().addDie(modelView.getDraftPoolDie(i));
        }catch (NoDieException e){
            println("Errore: dado non presente");
            return;
        }

        //TODO problema, come passo il valore di colore del nuovo dado??

    }

    /**
     * Use the tamponeDiamantato tool
     */
    private void tamponeDiamantato(){
        int i = chooseDraftpoolDie();
        int[] position = chooseBoardCellWithoutDie();
        ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.TAMPONEDIAMANTATO,i));
        setChanged();
        notifyObservers(clientMessage);
        println("Mossa inviata");
    }

    /**
     * Asck the player to chose a action
     */
    public void chooseMove(){
        println("------------------------------------------------------");
        println("E' il tuo turno, scelgi la mossa da effettuare:");
        println("0) Salta turno");
        println("1) Piazza un dado dalla riserva");
        println("2) Usa una carta strumento");
        println("3) Visualizza la tua plancia");
        println("4) Visualizza le plancie degli avversari");
        println("5) Visualizza la riserva dei dadi");
        println("6) Visualizza il tracciato dei dadi");
        println("7) Visualizza il le carte strumento");
        println("8) Visualizza il le carte obiettivo pubblico");
        println("9) Visualizza il tuo obiettivo privato");
        print("Scelta: ");
        int i = -1;
        do{
            System.out.println("lmao");
            i = InputUtils.getInt();
        }while(i < 0 || i > 9);
        println("Input ricevuto");

        switch (i){
            case 0:
                println("In attesa di mandarlo");
                setChanged();
                notifyObservers(new ClientMessage(new PlayerMove(Tool.SKIPTURN)));
                println("Mossa inviata");
                return;
            case 1:
                move(Tool.MOSSASTANDARD);
                break;
            case 2:
                showToolCards();
                println("Carta strumento scelta: ");
                int k = 4;
                do{
                    k = InputUtils.getInt();
                }while(k < 1 || k > 3);

                move((Tool) modelView.getTools().keySet().toArray()[k-1]);
                break;
            case 3:
                showPlayerBoard(modelView.getBoard(modelView.getPlayer(localID)));
                chooseMove();
                break;
            case 4:
                for(int j = 0; j < modelView.getPlayers().size(); j ++){
                    if(!modelView.getPlayers().get(j).getId().equals(localID)){
                        println("Board del player " + modelView.getPlayers().get(j).getNick());
                        showPlayerBoard(modelView.getBoard(modelView.getPlayers().get(j)));
                    }
                }
                chooseMove();
                break;
            case 5:
                showDraftPool();
                chooseMove();
                break;
            case 6:
                showRoundTrack();
                chooseMove();
                break;
            case 7:
                showToolCards();
                chooseMove();
                break;
            case 8:
                showPublicObjectives();
                chooseMove();
                break;
            case 9:
                showPrivateObjective();
                chooseMove();
                break;
            default:
                setChanged();
                notifyObservers(new ClientMessage(new PlayerMove(Tool.SKIPTURN)));
                println("Mossa inviata");

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        ServerMessage message = (ServerMessage) arg;
        switch (message.getMessageType()) {
            case ERROR:
                println("Errore: " + message.getErrorType().toString());
                if(message.getErrorType().equals(ErrorType.ILLEGALMOVE)){
                    println("Mossa inviata non valida, ripeti la scelta");
                    println("------------------------------------------------------");
                    chooseMove();
                }else{
                    println("Non e' il tuo turno");
                }
                break;
            case INITIALCONFIGSERVER:
                println("------------------------------------------------------");
                this.modelView = message.getModelView();

                println("\n\nLa tua board");
                showBoard(modelView.getBoard(modelView.getPlayer(localID)));
                showDraftPool();
                showToolCards();
                showPrivateObjective();
                showPublicObjectives();

                if(modelView.getPlayer(localID).isYourTurn()){
                    println("Sei il primo giocatore");
                    chooseMove();
                }else{
                    println("Attendi il tuo turno");
                }
                break;
            case BOARDTOCHOOSE:
                println("------------------------------------------------------");
                println("Scegli la tua plancia di gioco");
                for (int i = 0; i < message.getBoards().length; i++) {
                    println("Plancia " + (i+1));
                    showBoard(new PlayerBoard(message.getBoards()[i]));
                }

                println("Inserisci il numero della plancia scelta: ");
                int j;
                do{
                    j = InputUtils.getInt();
                } while (j < 1 || j > message.getBoards().length);

                setChanged();
                notifyObservers(new ClientMessage(message.getBoards()[j - 1]));
                break;
            case MODELVIEWUPDATE:
                println("------------------------------------------------------");
                LOGGER.log(Level.FINE,"ModelviewUpdate ricevuto");
                modelView = new ModelView(modelView, message.getModelView());

                println("La tua plancia: ");
                showPlayerBoard(modelView.getBoard(modelView.getPlayer(localID)));

                for(int k = 0; k < modelView.getPlayers().size(); k ++){
                    if(modelView.getPlayers().get(k).isYourTurn()){
                        LOGGER.log(Level.FINE,"E' il turno del giocatore " + modelView.getPlayers().get(k).getNick());
                    }
                }
                if(modelView.getPlayer(localID).isYourTurn()){
                    chooseMove();
                }else{
                    for(int h = 0; h < modelView.getPlayers().size(); h ++){
                        if(!modelView.getPlayers().get(h).getId().equals(localID)){
                            println("\nPlancia del giocatore: "+modelView.getPlayers().get(h).getNick());
                            showPlayerBoard(modelView.getBoard(modelView.getPlayers().get(h)));
                        }
                    }

                }
                break;
            default:
                LOGGER.log(Level.WARNING,"Messaggio ricevuto di tipo non elaborabile");
                break;
        }
    }

    /**
     * Called when the connection is closed
     */
    public void connectionClosed(){
        println("Connessione al server chiusa");
    }

    /**
     * Prompt the user for the connection detail to initialize the connection
     * @param clientNetwork the connection manager of the client
     */
    public void createConnection(ClientNetwork clientNetwork){
        println("Benvenuto, scegli il metodo di connessione: ");
        println("1) Socket");
        println("2) RMI");
        int i = 0;
        do{
            i = InputUtils.getInt();
        }while(i < 1 || i > 2);

        String address = "";
        int port = 0;
            while(!clientNetwork.isConnected()) {
                println("Inserisci l'indirizzo del server: ");
                boolean prova = true;
                Pattern pattern = Pattern.compile("((2[0-4]\\d|25[0-5]|[01]?\\d?\\d)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)");
                while (prova) {
                    address = InputUtils.getString();
                    if (pattern.matcher(address).matches())
                        prova = false;
                    else
                        println("Ip non corretto\nRiprova: ");

                }

                if (i == 1) {
                    do {
                        println("Inserisci la porta: ");
                        port = InputUtils.getInt();
                    }while(Server.available(port));
                    if(!clientNetwork.connectSocket(address, port)){
                        println("Connessione fallita");
                    }
                } else
                    if(!clientNetwork.connectRMI(address)){
                        println("Connessione fallita");
                    }
            }
            println("Connessione accettata");

        String nick;
        println("Inserisci il tuo nome: ");
        do{
            nick = InputUtils.getString();
        } while (nick.isEmpty());


        localID = readID();

        ClientMessage clientMessage = new ClientMessage(nick,localID);
        if(clientNetwork.sendMessage(clientMessage)){
            println("Nome utente inviato, attendi l'inizio della partita");
        }else{
            println("Errore connessione");
        }
    }

    private Long readID(){
        InputStream is = null;
        DataInputStream dis = null;
        try {
            is = new FileInputStream("playerID");

            dis = new DataInputStream(is);

            if(dis.available()>0) {
                return dis.readLong();
            }
        } catch(Exception e) {}finally {
            try{
                if(is != null)
                    is.close();
            }catch (Exception  e){}
            try{
                if(dis != null)
                    dis.close();
            }catch (Exception e){}
        }

        return generateID();//writeID(generateID());
    }

    public Long writeID(Long id){
        FileOutputStream fos = null;
        DataOutputStream dos = null;

        try {
            fos = new FileOutputStream("playerID");
            dos = new DataOutputStream(fos);

            dos.writeLong(id);
            dos.flush();
        } catch(Exception e){
            println("Errore scrittura id giocatore, controlla i permessi di scrittura della cartella");
        }finally {
            try{
                if(fos != null)
                    fos.close();
            }catch (Exception e){}
            try{
                if(dos != null)
                dos.close();
            }catch (Exception e){}
        }

        return id;
    }

    private Long generateID(){
        return (new Random()).nextLong();
    }
}
