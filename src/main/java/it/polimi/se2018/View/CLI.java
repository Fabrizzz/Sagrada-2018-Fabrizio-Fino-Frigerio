package it.polimi.se2018.View;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.server.Server;
import it.polimi.se2018.utils.InputUtils;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.MessageType;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.messages.*;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Command Line Interface
 * @author Alessio
 */
public class CLI extends View{
    private final String ANSI_RESET = "\u001B[0m";
    private Map<Color,String> colorMap = new HashMap<>();
    private ModelView modelView;
    private Long localID;

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

    /**
     * Show a playerboard to the player
     * @param playerBoard playerboard to show
     */
    public void showBoard(PlayerBoard playerBoard){
        System.out.println("\nPlancia dei dadi");
        System.out.println("___________|| Riga");

        for(int j = 0; j < 4; j ++){
            for(int i = 0; i < 5; i++){
                try{
                    System.out.print("|" + colorMap.get(playerBoard.getDie(j,i).getColor()) + playerBoard.getDie(j,i).getNumber().getInt() + ANSI_RESET);
                }catch(NoDieException e) {
                    System.out.print("| ");
                }
            }
            System.out.println("|| "+(j+1));
        }
        System.out.println("‾‾‾‾‾‾‾‾‾");
        System.out.println("|1|2|3|4|5|| Colonna");

        System.out.println("\nPlancia delle restrizioni");
        System.out.println("___________");


        for(int j = 0; j < 4; j ++){
            for(int i = 0; i < 5; i++){

                if(playerBoard.getRestriction(j,i).isNumberRestriction()){
                    System.out.print("|" + (((NumberRestriction) (playerBoard.getRestriction(j,i))).getNumber()).getInt());
                }else if(playerBoard.getRestriction(j,i).isColorRestriction()){
                    System.out.print("|" + colorMap.get(((ColorRestriction) playerBoard.getRestriction(j,i)).getColor()) + "x" + ANSI_RESET);
                }else{
                    System.out.print("| ");
                }

            }
            System.out.println("|");
        }
        System.out.println("‾‾‾‾‾‾‾‾‾‾");
    }

    /**
     * Show the draftpool to the player
     */
    public void showDraftPool(){
        System.out.println("Riserva dei dadi");

        System.out.print("Posizione:");
        for(int i = 0; i <  modelView.DraftPoolSize();i++){
            System.out.print("|"+(i+1));
        }
        System.out.println("|");

        System.out.print("Dadi:     ");
        for(int i = 0; i < modelView.DraftPoolSize(); i++){
            try{
                System.out.print("|" + colorMap.get(modelView.getDraftPoolDie(i).getColor()) + modelView.getDraftPoolDie(i).getNumber().getInt() + ANSI_RESET);
            }catch(NoDieException e){
                System.out.print("| ");
            }
        }
        System.out.println("|");
    }

    /**
     * Show the toolcards to the player
     */
    public void showToolCards(){
        System.out.println("Carte strumento:");
        for (int i = 0; i < modelView.getTools().size(); i++) {
            System.out.print("Nome: ");
            System.out.println(modelView.getTools().keySet().toArray()[i].toString());
            System.out.print("Descrizione: ");
            System.out.println(((Tool) modelView.getTools().keySet().toArray()[i]).getDescription());
            System.out.println("Usato: "+modelView.getTools().get(modelView.getTools().keySet().toArray()[i]) + "\n");
        }
    }

    /**
     * Show the public objectives cards
     */
    public void showPublicObjectives(){
        System.out.println("Carte obiettivo pubbliche:");
        for(int i = 0; i < modelView.getPublicObjective().size(); i ++){
            System.out.println("Numero " + (i+1));
            System.out.println(modelView.getPublicObjective().get(i).getObjectiveName() + "\n");
        }
    }

    /**
     * Show the player private objective
     */
    private void showPrivateObjective(){
        System.out.println("Colore obiettivo privato: " /*+ modelView.getPlayer(localID).*/);//TODO
    }

    /**
     * Show the round track to the player
     */
    public void showRoundTrack(){
        System.out.println("Tracciato dei dadi:");
        for(int i = 0; i < modelView.getRound(); i ++){
            System.out.println("Round " + (i+1));
            for(int j = 0; j < modelView.getRoundTrack().numberOfDice(i); j ++){
                try{
                    System.out.println("|" + modelView.getRoundTrackDie(i,j));
                }catch (NoDieException e){}
            }
            System.out.println("|||");
        }
    }

    public int[] chooseRoundTrackDie(){
        int[] position = new int[2];
        showRoundTrack();
        System.out.println("Inserisci il numero del round: ");
        do {
            position[0] = InputUtils.getInt();
        }while (position[0] > 0 && position[0] < modelView.getRound());

        System.out.println("Inserisci la posizione del dado: ");
        do {
            position[1] = InputUtils.getInt();
        }while (position[1] > 0 && position[1] < modelView.getRoundTrack().numberOfDice(position[0] - 1));

        position[0] --;
        position[1] --;

        return position;
    }

    /**
     * Let the player choose a die from the draftpool
     * @return the index of the chosen die
     */
    public int chooseDraftpoolDie(){
        System.out.println("Scegli il dado dalla riserva");
        showDraftPool();
        System.out.println("Inserisci la posizione del dado scelto:");
        int i = InputUtils.getInt();
        while (i < 1 || i > modelView.DraftPoolSize()) {
            System.out.println("Errore, inserisci una posizione corretta");
            i = InputUtils.getInt();
        }
        return i - 1;

    }

    /**
     * Let the player choose a cell in his playerboard
     * @param withDie Require the player to choose a cell containing a die or not
     * @return  the array containing the row and column coordinates to identify the cell
     */
    public int[] chooseBoardCell(Boolean withDie){
        int[] position = new int[2];
        boolean repeat = true;
        do {
            showBoard(modelView.getBoard(modelView.getPlayer(localID)));
            System.out.println("Inserisci l'indice di riga: ");
            position[0] = InputUtils.getInt();
            while (position[0] < 1 || position[0] > 4) {
                System.out.println("Errore, inserisci un indice corretto");
                position[0] = InputUtils.getInt();
            }

            System.out.println("Inserisci l'indice di colonna: ");
            position[1] = InputUtils.getInt();
            while (position[1] < 1 || position[1] > 5) {
                System.out.println("Errore, inserisci un indice corretto");
                position[1] = InputUtils.getInt();
            }
            if(withDie){
                if(modelView.getBoard(modelView.getPlayer(localID)).containsDie(position[0] - 1,position[1] - 1)){
                    repeat = false;
                }else{
                    repeat = true;
                    System.out.println("Errore: nessun dado presente in posizione riga: " + position[0] + ", colonna: " + position[1] + ". Ripetere la scelta");
                }
            }else{
                if(!modelView.getBoard(modelView.getPlayer(localID)).containsDie(position[0] - 1,position[1] - 1)){
                    repeat = false;
                }else{
                    repeat = true;
                    System.out.println("Errore: dado presente in posizione riga: " + position[0] + ", colonna: " + position[1] + ". Ripetere la scelta");
                }

            }
        }while(repeat);

        position[0] --;
        position[1] --;
        return position;
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

        System.out.println("Scelgi la dove piazzare il dado");
        int[] position = chooseBoardCell(false);

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], i));
        setChanged();
        notifyObservers(clientMessage);
        System.out.println("Mossa inviata");
    }

    /**
     * Execute one of the following actions: skip turn, martelletto tool, tenaglia a rotelle tool
     * @param tool tool to use
     */
    public void skipMartellettoTenagliaMove(Tool tool){
        ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool));
        setChanged();
        notifyObservers();
        System.out.println("Mossa inviata");
    }

    /**
     * Use the pinza sgrossatrice tool
     * @return
     */
    public boolean sgrossatriceMove(){
       int position = chooseDraftpoolDie();
       try {
            System.out.println("Valore del dado selezionato: " + modelView.getDraftPoolDie(position).getNumber().getInt());
            int scelta = 3;
            while(scelta != 0 && scelta != 1){
                System.out.println("Inserisci 0 per diminuire il valore del dado selezionato, 1 per aumentare il valore del dado");
                scelta = InputUtils.getInt();
            }

            Boolean aumento = false;
            if(scelta == 0){
                aumento = false;
            }else{
                aumento = true;
            }

            ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.PINZASGROSSATRICE,position,aumento));
            setChanged();
            notifyObservers(clientMessage);
            System.out.println("Mossa inviata");

            return true;
       }catch(NoDieException e){
           return false;
       }

    }

    /**
     * Use one of the following tools: pennello per eglomise, alesatore per lamina di rame,lathekin, taglierina manuale
     * @param tool tool to use
     */
    public void pennelloAlesatoreLeathekinManualeMove(Tool tool){
        boolean secondaMossa = false;
        System.out.println("Scegli il primo dado da muovere");
        int[] position = chooseBoardCell(true);
        System.out.println("Scegli dove piazzare primo il dado");
        int[] newPosition = chooseBoardCell(false);
        ClientMessage clientMessage;

        if(tool == Tool.LATHEKIN){
            secondaMossa = true;
        }else if(tool == Tool.TAGLIERINAMANUALE){
            System.out.println("Vuoi scegliere un secondo dado da muovere? 0 no, 1 si");
            int scelta = 2;
            do{
                scelta = InputUtils.getInt();
            }while(scelta != 0 || scelta != 1);

            if(scelta == 1) {
                secondaMossa = true;
            }else {
                secondaMossa = false;
            }
        }

        if(secondaMossa){
            System.out.println("Scegli il secondo dado da muovere");
            int[] position2 = chooseBoardCell(true);
            System.out.println("Scegli dove piazzare il secondo dado");
            int[] newPosition2 = chooseBoardCell(false);
            clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], newPosition[0], newPosition[1],new PlayerMove(tool, position2[0], position2[1], newPosition2[0], newPosition2[1])));
        }else{
            clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], newPosition[0], newPosition[1]));
        }

        setChanged();
        notifyObservers(clientMessage);
        System.out.println("Mossa inviata");
    }

    /**
     * Use one the taglierina circolare tool
     */
    public void taglierinaCircolareMove(){
        int i = chooseDraftpoolDie();
        int[] roundPosition = chooseRoundTrackDie();

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(i,roundPosition[0],roundPosition[1],Tool.TAGLIERINACIRCOLARE));
        setChanged();
        notifyObservers(clientMessage);
        System.out.println("Mossa inviata");
    }

    /**
     * Use the pennello per pasta salda tool
     */
    public void pennelloPastaSaldaMove(){
        int i = chooseDraftpoolDie();
        NumberEnum newNum = NumberEnum.values()[(new Random()).nextInt(NumberEnum.values().length)];
        System.out.println("Il nuovo valore del dado e': " + newNum.getInt());
        int[] position = chooseBoardCell(false);

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(position[0],position[1],i,newNum,Tool.PENNELLOPERPASTASALDA));
        setChanged();
        notifyObservers(clientMessage);
        System.out.println("Mossa inviata");
    }

    /**
     * Use the diluente per pasta salda tool
     */
    public void diluentePerPastaSaldaMove(){
        int i = chooseDraftpoolDie();
        try {
            modelView.getDiceBag().addDie(modelView.getDraftPoolDie(i));
        }catch (NoDieException e){
            System.out.println("Errore: dado non presente");
            return;
        }

        //problema, come passo il valore di colore del nuovo dado??

    }

    /**
     * Use the tamponeDiamantato tool
     */
    public void tamponeDiamantato(){
        int i = chooseDraftpoolDie();

        ClientMessage clientMessage = new ClientMessage(new PlayerMove(Tool.TAMPONEDIAMANTATO,i));
        setChanged();
        notifyObservers(clientMessage);
        System.out.println("Mossa inviata");
    }

    /**
     * Asck the player to chose a action
     */
    public void chooseMove(){
        System.out.println("E' il tuo turno, scelgi la mossa da effettuare:");
        System.out.println("0) Salta turno");
        System.out.println("1) Piazza un dado dalla riserva");
        System.out.println("2) Usa una carta strumento");
        System.out.println("3) Visualizza la tua plancia");
        System.out.println("4) Visualizza le plancie degli avversari");
        System.out.println("5) Visualizza la riserva dei dadi");
        System.out.println("6) Visualizza il tracciato dei dadi");
        System.out.println("7) Visualizza il le carte strumento");
        System.out.println("8) Visualizza il le carte obiettivo pubblico");
        System.out.println("9) Visualizza il tuo obiettivo privato");
        System.out.print("Scelta: ");
        int i = 3;
        do{
            i = InputUtils.getInt();
        }while(i < 0 || i > 9);
        System.out.println("");

        switch (i){
            case 0:
                setChanged();
                notifyObservers(new ClientMessage(new PlayerMove(Tool.SKIPTURN)));
                System.out.println("Mossa inviata");
                return;
            case 1:
                move(Tool.MOSSASTANDARD);
                break;
            case 2:
                showToolCards();
                System.out.println("Carta strumento scelta: ");
                int k = 4;
                do{
                    k = InputUtils.getInt();
                }while(k < 1 || k > 3);

                move((Tool) modelView.getTools().keySet().toArray()[k]);
                break;
            case 3:
                showBoard(modelView.getBoard(modelView.getPlayer(localID)));
                chooseMove();
                break;
            case 4:
                for(int j = 0; j < modelView.getPlayers().size(); j ++){
                    if(modelView.getPlayers().get(j).getId() != localID){
                        System.out.println("Board del player " + modelView.getPlayers().get(j).getNick());
                        showBoard(modelView.getBoard(modelView.getPlayers().get(j)));
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
                System.out.println("Mossa inviata");

        }

        System.out.println("La tua plancia: ");
        showBoard(modelView.getBoard(modelView.getPlayer(localID)));
    }
    @Override
    public void update(Observable o, Object arg) {
        switch (((ServerMessage) arg).getMessageType()){
            case ERROR:
                System.out.println("Errore: " + ((ServerMessage) arg).getErrorType().toString());
                if(modelView.getPlayer(localID).isYourTurn()){
                    System.out.println("Mossa inviata non valida, ripeti la scelta");
                    chooseMove();
                }
                break;
            case INITIALCONFIGSERVER:
                this.modelView = ((ServerMessage) arg).getModelView();

                System.out.println("La tua board");
                showBoard(modelView.getBoard(modelView.getPlayer(localID)));
                showDraftPool();
                showToolCards();
                showPrivateObjective();
                showPublicObjectives();

                if(modelView.getPlayer(localID).isYourTurn()){
                    System.out.println("Sei il primo giocatore");
                    chooseMove();
                }else{
                    System.out.println("Attendi il tuo turno");
                }
                break;
            case BOARDTOCHOOSE:
                System.out.println("Scegli la tua plancia di gioco");
                for(int i = 0; i < ((SelectBoardMessage) arg).getBoards().length; i ++){
                    System.out.println("Plancia " + (i+1));
                    showBoard(new PlayerBoard(((SelectBoardMessage) arg).getBoards()[i]));
                }

                System.out.println("Inserisci il numero della plancia scelta: ");
                int j = 0;
                do{
                    j = InputUtils.getInt();
                }while(j < 1 || j > ((SelectBoardMessage) arg).getBoards().length);

                setChanged();
                notifyObservers(new ChosenBoardMessage(MessageType.CHOSENBOARD,new PlayerBoard(((SelectBoardMessage) arg).getBoards()[j])));
                break;
            case MODELVIEWUPDATE:
                modelView = new ModelView(modelView, ((ServerMessage) arg).getModelView());
                if(modelView.getPlayer(localID).isYourTurn()){
                    System.out.println("E' il tuo turno");
                    chooseMove();
                }
                break;
        }
    }

    /**
     * Called when the connection is closed
     */
    public void connectionClosed(){
        System.out.println("Connessione al server chiusa");
    }

    /**
     * Prompt the user for the connection detail to initialize the connection
     * @param clientNetwork the connection manager of the client
     */
    public void createConnection(ClientNetwork clientNetwork){
        System.out.println("Benvenuto, scegli il metodo di connessione: ");
        System.out.println("1) Socket");
        System.out.println("2) RMI");
        int i = 0;
        do{
            i = InputUtils.getInt();
        }while(i < 1 || i > 2);

        String address = "";
        int port = 0;
            while(!clientNetwork.isConnected()) {
                System.out.println("Inserisci l'indirizzo del server: ");
                boolean prova = true;
                Pattern pattern = Pattern.compile("((2[0-4]\\d|25[0-5]|[01]?\\d?\\d)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)");
                while (prova) {
                    address = InputUtils.getString();
                    if (pattern.matcher(address).matches())
                        prova = false;
                    else
                        System.out.println("Ip non corretto\nRiprova: ");

                }

                if (i == 1) {
                    do {
                        System.out.println("Inserisci la porta: ");
                        port = InputUtils.getInt();
                    }while(Server.available(port));
                    if(!clientNetwork.connectSocket(address, port)){
                        System.out.println("Connessione fallita");
                    }
                } else
                    if(!clientNetwork.connectRMI(address)){
                        System.out.println("Connessione fallita");
                    }
            }
            System.out.println("Connessione accettata");

        String nick;
        System.out.println("Inserisci il tuo nome: ");
        do{
            nick = InputUtils.getString();
        } while (nick.isEmpty());


        localID = readID();
        System.out.println("Player id = " + localID);

        ClientMessage clientMessage = new ClientMessage(nick,localID);
        if(clientNetwork.sendMessage(clientMessage)){
            System.out.println("Nome utente inviato, attendi l'inizio della partita");
        }else{
            System.out.println("Errore connessione");
        }
    }

    public Long readID(){
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
                is.close();
            }catch (Exception e){}
            try{
                dis.close();
            }catch (Exception e){}
        }

        return writeID(generateID());
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
            System.out.println("Errore scrittura id giocatore, controlla i permessi di scrittura della cartella");
        }finally {
            try{
                fos.close();
            }catch (Exception e){}
            try{
                dos.close();
            }catch (Exception e){}
        }

        return id;
    }

    public Long generateID(){
        return (new Random()).nextLong();
    }
}
