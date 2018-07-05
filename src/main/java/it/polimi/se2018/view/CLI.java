package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.server.Server;
import it.polimi.se2018.utils.InputUtils;
import it.polimi.se2018.utils.JSONUtils;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlreadySetDie;
import it.polimi.se2018.utils.exceptions.EmptyBagException;
import it.polimi.se2018.utils.exceptions.FullBoardException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.ClientMessage;
import it.polimi.se2018.utils.messages.PlayerMove;
import it.polimi.se2018.utils.messages.ServerMessage;

import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command Line Interface
 * @author Alessio
 */
public class CLI extends View{
    private static final String ANSI_RESET = "\u001B[0m";
    private Map<Color, String> colorMap = new EnumMap<>(Color.class);
    private ModelView modelView;
    private Long localID;
    private String nick;
    private boolean hasUsedTenaglia = false;
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

        println("Inserisci il tuo nome: ");
        do{
            nick = InputUtils.getString();
        } while (nick.isEmpty());

        this.localID = JSONUtils.readID(nick);
    }

    /**
     * Print the string and append a newline on the end
     * @param string string to print
     */
    private void println(String string){
        System.out.println(string);
    }

    /**
     * Print the string and append
     * @param string string to print
     */
    private void print(String string){
        System.out.print(string);
    }
    
    /**
     * Show a playerboard
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
     * Show the board containing the restriction
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
        println("Colore obiettivo privato: " + modelView.getPrivateObjective());
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

    /**
     * Let the player choose a die from the roundTrack
     * @return the array containing the round and the position of the die in the round
     * @throws NoDieException if the roundtrack is empty
     */
    private int[] chooseRoundTrackDie() throws NoDieException{
        int[] position = new int[2];
        boolean empty = true;
        for (int i = 0; i <= modelView.getRound(); i++) {
            if (modelView.getRoundTrack().numberOfDice(i) > 0)
                empty = false;
        }
        if (empty) {
            println("Errore, il tracciato dei dadi e' vuoto");
            throw new NoDieException();
        }
        showRoundTrack();
        do {
            println("Scegli la round position da cui prendere il dado: ");
            position[0] = InputUtils.getInt();
            position[0]--;
        }
        while (position[0] < 0 || position[0] > modelView.getRound() || modelView.getRoundTrack().numberOfDice(position[0]) == 0);


        println("Inserisci la posizione del dado: ");
        do {
            position[1] = InputUtils.getInt();
            position[1]--;
        } while (position[1] < 0 || position[1] >= modelView.getRoundTrack().numberOfDice(position[0]));



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
     *
     * Let the player choose a cell in his playerboard
     * @return  the array containing the row and column coordinates to identify the cell
     * @throws NoDieException if the board is empty
     */
    private int[] chooseBoardCellWithDie() throws NoDieException{
        int[] position = new int[2];
        boolean repeat;

        if(modelView.getBoard(modelView.getPlayer(localID)).isEmpty()){
            println("La plancia e' vuota, non puoi eseguire questa mossa");
            LOGGER.log(Level.FINE,"La plancia e' vuota");
            throw new NoDieException();
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

    /**
     * Let the player choose a row
     * @return the chosen row
     */
    private int chooseRow(){
        int row;
        println("Inserisci l'indice di riga: ");
        row = InputUtils.getInt();
        while (row < 1 || row > 4) {
            println("Errore, inserisci un indice corretto");
            row = InputUtils.getInt();
        }
        return row - 1;
    }

    /**
     * Let the player choose a column
     * @return the chosen column
     */
    private int chooseColumn(){
        int column;
        println("Inserisci l'indice di colonna: ");
        column = InputUtils.getInt();
        while (column < 1 || column > 5) {
            println("Errore, inserisci un indice corretto");
            column = InputUtils.getInt();
        }

        return column - 1;
    }

    /**
     * Return if the board is full
     * @param board board to search
     * @return true if is full, false otherwise
     */
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

    /**
     * Let the player choose an empty cell in the board
     * @return the position of the cell
     * @throws AlreadySetDie if the board is full
     */
    private int[] chooseBoardCellWithoutDie() throws FullBoardException {
        int[] position = new int[2];
        boolean repeat;

        if(boardIsFull(modelView.getBoard(modelView.getPlayer(localID)))){
            println("Errore, la board e' piena non puoi eseguire questa mossa");
            LOGGER.log(Level.FINE,"La plancia e' piena");
            throw new FullBoardException();
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
     * Use a tool
     * @param tool type of action to be used
     */
    public void move(Tool tool){
        switch (tool) {
            case MOSSASTANDARD:
            case RIGAINSUGHERO:
                normalSugheroMove(tool);
                break;
            case SKIPTURN:
            case MARTELLETTO:
            case TENAGLIAAROTELLE:
                skipMartellettoTenagliaMove(tool);
                break;
            case PINZASGROSSATRICE:
                sgrossatriceMove();
                break;
            case PENNELLOPEREGLOMISE:
            case ALESATOREPERLAMINADIRAME:
            case TAGLIERINAMANUALE:
            case LATHEKIN:
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
        if(tool.equals(Tool.MOSSASTANDARD) && modelView.isNormalMove()){
            println("Errore, hai gia eseguito una mossa normale, passa il turno o usa una carta strumento");
            chooseMove();
            return;
        }

        int i = chooseDraftpoolDie();

        println("Scegli dove piazzare il dado");
        try{
            int[] position = chooseBoardCellWithoutDie();
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], i));
            setChanged();
            notifyObservers(clientMessage);
            println("Mossa inviata");
        } catch (FullBoardException e) {
            println("Errore, la plancia e' piena, non puoi usare questo tool");
            chooseMove();
        }
    }

    /**
     * Execute one of the following actions: skip turn, martelletto tool, tenaglia a rotelle tool
     * @param tool tool to use
     */
    private void skipMartellettoTenagliaMove(Tool tool){
        if ((tool == Tool.TENAGLIAAROTELLE && (!modelView.isFirstTurn() || !modelView.isNormalMove()))
                || (tool == Tool.MARTELLETTO && (modelView.isFirstTurn() || modelView.isNormalMove()))) {
            println("Errore: Non puoi eseguire questa mossa");
            chooseMove();
        } else {
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool));
            if (tool == Tool.TENAGLIAAROTELLE)
                hasUsedTenaglia = true;
            setChanged();
            notifyObservers(clientMessage);
            println("Mossa inviata");


        }
    }

    /**
     * Use the pinzasgrossatrice tool
     */
    private void sgrossatriceMove(){
       int position = chooseDraftpoolDie();
       try {
            println("Valore del dado selezionato: " + modelView.getDraftPoolDie(position).getNumber().getInt());
           int scelta;
           do {
                println("Inserisci 0 per diminuire il valore del dado selezionato, 1 per aumentare il valore del dado");
                scelta = InputUtils.getInt();
           } while (scelta != 0 && scelta != 1);

            Boolean aumento = (scelta != 0);

            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.PINZASGROSSATRICE,position,aumento));
            setChanged();
            notifyObservers(clientMessage);
            println("Mossa inviata");

       }catch(NoDieException e){
           LOGGER.log(Level.SEVERE, "Nessun dado presente");
           println("Errore, dado non presente nel draftpool, mossa annullata");
           chooseMove();
       }

    }

    /**
     * Use one of the following tools: pennello per eglomise, alesatore per lamina di rame,lathekin, taglierina manuale
     * @param tool tool to use
     */
    private void pennelloAlesatoreLeathekinManualeMove(Tool tool){
        try{
            boolean secondaMossa = false;
            println("Scegli il primo dado da muovere");
            int[] position = chooseBoardCellWithDie();

            println("Scegli dove piazzare primo il dado");
            int[] newPosition = chooseBoardCellWithoutDie();

            if(tool == Tool.LATHEKIN){
                secondaMossa = true;
            }else if(tool == Tool.TAGLIERINAMANUALE){
                println("Vuoi scegliere un secondo dado da muovere? 0 no, 1 si");
                int scelta;
                do{
                    scelta = InputUtils.getInt();
                }while(scelta != 0 && scelta != 1);

                secondaMossa = (scelta == 1);
            }

            ClientMessage clientMessage;
            if(secondaMossa){
                println("Scegli il secondo dado da muovere");
                int[] position2 = chooseBoardCellWithDie();
                println("Scegli dove piazzare il secondo dado");
                int[] newPosition2 = chooseBoardCellWithoutDie();
                clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], newPosition[0], newPosition[1],new PlayerMove(tool, position2[0], position2[1], newPosition2[0], newPosition2[1])));
            }else{
                clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], newPosition[0], newPosition[1]));
            }

            setChanged();
            notifyObservers(clientMessage);
            println("Mossa inviata");
        } catch (FullBoardException e) {
            println("Errore: la plancia e' piena, non puoi usare questo tool");
            chooseMove();
        }catch (NoDieException e){
            println("Errore: la plancia e' vuota, non puoi usare questo tool");
            chooseMove();
        }

    }

    /**
     * Use one the taglierina circolare tool
     */
    private void taglierinaCircolareMove(){
        try{
            int i = chooseDraftpoolDie();
            int[] roundPosition = chooseRoundTrackDie();

            ClientMessage clientMessage = new ClientMessage(new PlayerMove(i, roundPosition[0], roundPosition[1], Tool.TAGLIERINACIRCOLARE));
            setChanged();
            notifyObservers(clientMessage);
            println("Mossa inviata");
        }catch (NoDieException e){
            println("Errore: la plancia e' vuota, non puoi usare questo tool");
            chooseMove();
        }

    }

    /**
     * Use the pennello per pasta salda tool
     */
    private void pennelloPastaSaldaMove(){
        try{
            int i = chooseDraftpoolDie();

            Die die = modelView.getDraftPoolDie(i);
            die.reRoll();
            println("Il nuovo valore del dado e': " + die.getNumber().getInt());
            boolean check = true;
            PlayerBoard board = modelView.getBoard(modelView.getPlayer(localID));
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
            ClientMessage clientMessage;
            if (!check) {
                int[] position = chooseBoardCellWithoutDie();
                clientMessage = new ClientMessage(new PlayerMove(position[0], position[1], i, die.getNumber(), Tool.PENNELLOPERPASTASALDA));
            } else {
                System.out.println("Il dado viene riposto in riserva");
                clientMessage = new ClientMessage(new PlayerMove(i, die.getNumber(), Tool.PENNELLOPERPASTASALDA));
            }
            setChanged();
            notifyObservers(clientMessage);
            println("Mossa inviata");
        } catch (FullBoardException e) {
            println("Errore: la plancia e' piena, non puoi usare questo tool");
            chooseMove();
        } catch (NoDieException e) {
            LOGGER.log(Level.SEVERE, "la chooseDraftPoolDie non funziona");
        }
    }

    /**
     * Use the diluente per pasta salda tool
     */
    private void diluentePerPastaSaldaMove(){
        int pos = chooseDraftpoolDie();
        try {
            Die die = modelView.getDiceBag().getFirst();
            println("Il dado pescato dalla dicebag è " + die.getColor().getColorString());
            int val;
            do {
                println("Scegli il valore del dado: ");
                val = InputUtils.getInt();
            } while (val < 1 || val > 6);
            die.setNumber(NumberEnum.getNumber(val));

            boolean check = true;
            PlayerBoard board = modelView.getBoard(modelView.getPlayer(localID));
            for (int r = 0; r < 4 && check; r++) {
                for (int c = 0; c < 5 && check; c++) {
                    if ((board.verifyInitialPositionRestriction(r, c) && board.isEmpty()) ||
                            (!board.containsDie(r, c) &&
                                    board.verifyNumberRestriction(die, r, c) &&
                                    board.verifyColorRestriction(die, r, c) &&
                                    board.verifyNearCellsRestriction(die, r, c) &&
                                    board.verifyPositionRestriction(r, c)))
                        check = false;
                }
            }
            if (check) {
                println("Non è possibile posizionarlo, viene riposto in riserva");
                setChanged();
                notifyObservers(new ClientMessage(new PlayerMove(pos, NumberEnum.getNumber(val), Tool.DILUENTEPERPASTASALDA)));
            } else {
                int[] pair = chooseBoardCellWithoutDie();
                setChanged();
                notifyObservers(new ClientMessage(new PlayerMove(pair[0], pair[1], pos, NumberEnum.getNumber(val), Tool.DILUENTEPERPASTASALDA)));
            }
            println("Mossa inviata");

        } catch (EmptyBagException e) {
            LOGGER.log(Level.SEVERE, "DiceBag Vuota");
        } catch (FullBoardException e) {
            LOGGER.log(Level.SEVERE, "La board è piena quando dovrebbe invece avere almeno una cella dove mettere il dado");
        }


    }

    /**
     * Use the tamponeDiamantato tool
     */
    private void tamponeDiamantato(){
        int i = chooseDraftpoolDie();
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
        println("E' il tuo turno, scegli la mossa da effettuare:");
        if(modelView.isNormalMove() || modelView.isUsedTool())
            println("0) Finisci turno");
        else
            println("0) Salta turno");
        if(!modelView.isNormalMove())
            println("1) Piazza un dado dalla riserva");
        if(!modelView.isUsedTool())
            println("2) Usa una carta strumento");
        println("3) Visualizza la tua plancia");
        println("4) Visualizza le plance degli avversari");
        println("5) Visualizza la riserva dei dadi");
        println("6) Visualizza il tracciato dei dadi");
        println("7) Visualizza le carte strumento");
        println("8) Visualizza le carte obiettivo pubblico");
        println("9) Visualizza il tuo obiettivo privato");
        print("Scelta: ");
        int i;
        do{
            i = InputUtils.getInt();
        }while(i < 0 || i > 9);
        println("Input ricevuto");

        switch (i){
            case 0:
                println("In attesa di inviare la mossa");
                setChanged();
                notifyObservers(new ClientMessage(new PlayerMove(Tool.SKIPTURN)));
                println("Mossa inviata");
                return;
            case 1:
                if(!modelView.isNormalMove())
                    move(Tool.MOSSASTANDARD);
                else
                    chooseMove();
                break;
            case 2:
                if(!modelView.isUsedTool()){
                    showToolCards();
                    println("Carta strumento scelta: ");
                    int k;
                    do{
                        k = InputUtils.getInt();
                    }while(k < 1 || k > 3);

                    move((Tool) modelView.getTools().keySet().toArray()[k-1]);
                }
                else
                    chooseMove();
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
                    println(message.getBoards()[i].getName());
                    showBoard(new PlayerBoard(message.getBoards()[i]));
                    println("Difficoltà della board: " + message.getBoards()[i].getTokens());
                    System.out.println();
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

                for (int k = 0; k < modelView.getPlayers().size(); k++) {
                    if (modelView.getPlayers().get(k).isYourTurn()) {
                        LOGGER.log(Level.FINE, "E' il turno del giocatore " + modelView.getPlayers().get(k).getNick());
                    }
                }
                if (modelView.getPlayer(localID).isYourTurn()) {
                    if (hasUsedTenaglia) {
                        hasUsedTenaglia = false;
                        move(Tool.MOSSASTANDARD);
                    } else
                        chooseMove();
                } else {
                    for (int h = 0; h < modelView.getPlayers().size(); h++) {
                        if (!modelView.getPlayers().get(h).getId().equals(localID)) {
                            println("\nPlancia del giocatore: " + modelView.getPlayers().get(h).getNick());
                            showPlayerBoard(modelView.getBoard(modelView.getPlayers().get(h)));
                        }
                        }
                    }
                break;
            case HASDISCONNECTED:
                println("Il giocatore " +  message.getDisconnectedPlayer() + " si e' disconnesso");
                //questo try non è necessario, in quanto non ricevi aggiornamenti sulla modelview alla disconnessione di qualcuno
                /*try {
                    if (modelView.getPlayer(localID).isYourTurn()) {
                        chooseMove();
                    }
                }catch (NullPointerException e){
                    LOGGER.log(Level.WARNING,"Null pointer has disconnected");
                }*/
                break;
            case HASRICONNECTED:
                println("Il giocatore " +  message.getDisconnectedPlayer() + " si e' riconnesso");
                break;
            case ENDGAME:
                try{
                    int top = 0;
                    println("------------------------------------------------------");
                    println("Gioco terminato");
                    println("Punteggi:");
                    for(String nick : message.getScores().keySet()){
                        if(message.getScores().get(nick) > top){
                            top = message.getScores().get(nick);
                        }
                        println(nick + " : " + message.getScores().get(nick));
                    }

                    for(String nick : message.getScores().keySet()){
                        if(message.getScores().get(nick) == top){
                            println("Il giocatore " + nick + " ha vinto");
                        }
                    }
                }catch (NullPointerException e){
                    LOGGER.log(Level.WARNING,"mappa scores mancante");
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
        int i;
        do{
            i = InputUtils.getInt();
        }while(i < 1 || i > 2);

        String address;
        int port;
            while(!clientNetwork.isConnected()) {
                println("Inserisci l'indirizzo del server: ");

                    address = InputUtils.getString();


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


        ClientMessage clientMessage = new ClientMessage(nick,localID);
        if(clientNetwork.sendMessage(clientMessage)){
            println("Nome utente inviato, attendi l'inizio della partita");
        }else{
            println("Errore connessione");
        }
    }
}
