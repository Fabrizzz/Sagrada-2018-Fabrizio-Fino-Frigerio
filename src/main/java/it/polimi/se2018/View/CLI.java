package it.polimi.se2018.View;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.model.*;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.ServerMessage;
import it.polimi.se2018.utils.enums.*;

import java.util.Random;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.*;

/**
 * Command Line Interface
 * @author Alessio
 */
public class CLI extends View{
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_BLUE = "\u001B[34m";
    private final String ANSI_PURPLE = "\u001B[35m";
    private Scanner input;
    private ModelView modelView;
    private Long localID;

    /**
     * Constructor
     */
    public CLI(){
        input = new Scanner(System.in);
    }

    /**
     * Return a string containing the characters necessary to change the color of the text printed in the command line
     * @param color color
     * @return the string of characters to change the color of the command line text
     */
    public String getColor(Color color){
        switch (color) {
            case BLUE:
                return ANSI_BLUE;
            case RED:
                return ANSI_RED;
            case GREEN:
                return ANSI_GREEN;
            case YELLOW:
                return ANSI_YELLOW;
            case PURPLE:
                return ANSI_PURPLE;
            default:
                return ANSI_RESET;
        }
    }

    /**
     * Show a playerboard to the player
     * @param playerBoard playerboard to show
     */
    public void showBoard(PlayerBoard playerBoard){
        System.out.println("\nDice board");
        System.out.println("___________|| Riga");

        for(int j = 0; j < 4; j ++){
            for(int i = 0; i < 5; i++){
                try{
                    System.out.print("|" + getColor(playerBoard.getDie(j,i).getColor()) + playerBoard.getDie(j,i).getNumber().getInt() + ANSI_RESET);
                }catch(NoDieException e) {
                    System.out.print("| ");
                }
            }
            System.out.println("|| "+(j+1));
        }
        System.out.println("‾‾‾‾‾‾‾‾‾");
        System.out.println("|1|2|3|4|5|| Colonna");

        System.out.println("\nRestriction board");
        System.out.println("___________");


        for(int j = 0; j < 4; j ++){
            for(int i = 0; i < 5; i++){

                if(playerBoard.getRestriction(j,i).isNumberRestriction()){
                    System.out.print("|" + (((NumberRestriction) (playerBoard.getRestriction(j,i))).getNumber()).getInt());
                }else if(playerBoard.getRestriction(j,i).isColorRestriction()){
                    System.out.print("|" + getColor(((ColorRestriction) playerBoard.getRestriction(j,i)).getColor()) + "x" + ANSI_RESET);
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
        System.out.println("Draftpool");

        System.out.print("Positione:");
        for(int i = 0; i <  modelView.DraftPoolSize();i++){
            System.out.print("|"+(i+1));
        }
        System.out.println("|");

        System.out.print("Dadi:     ");
        for(int i = 0; i < modelView.DraftPoolSize(); i++){
            try{
                System.out.print("|" + getColor(modelView.getDraftPoolDie(i).getColor()) + modelView.getDraftPoolDie(i).getNumber().getInt() + ANSI_RESET);
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
        System.out.println("Tool cards");
        for (int i = 0; i < modelView.getTools().size(); i++) {
            System.out.print("Nome: ");
            System.out.println(modelView.getTools().keySet().toArray()[i].toString());
            System.out.print("Descrizione: ");
            System.out.println(((Tool) modelView.getTools().keySet().toArray()[i]).getDescription());
            System.out.println("Usato: "+modelView.getTools().get(modelView.getTools().keySet().toArray()[i]) + "\n");
        }
    }

    /**
     * Show the round track to the player
     */
    public void showRoundTrack(){
        System.out.println("Tracciato dadi:");
        for(int i = 0; i < modelView.getRound(); i ++){
            System.out.println("Round " + (i+1));
            for(int j = 0; j < modelView.getRoundTrack().numberOfDice(i); j ++){
                System.out.println("|" + modelView.getRoundTrackDie(i,j));
            }
            System.out.println("|||");
        }
    }

    public int[] chooseRoundTrackDie(){
        int[] position = new int[2];
        showRoundTrack();
        System.out.println("Inserisci il numero del round: ");
        do {
            position[0] = input.nextInt();
        }while (position[0] > 0 && position[0] < modelView.getRound());

        System.out.println("Inserisci la posizione del dado: ");
        do {
            position[1] = input.nextInt();
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
        int i = input.nextInt();
        while (i < 1 || i > modelView.DraftPoolSize()) {
            System.out.println("Errore, inserisci una posizione corretta");
            i = input.nextInt();
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
            position[0] = input.nextInt();
            while (position[0] < 1 || position[0] > 4) {
                System.out.println("Errore, inserisci un indice corretto");
                position[0] = input.nextInt();
            }

            System.out.println("Inserisci l'indice di colonna: ");
            position[1] = input.nextInt();
            while (position[1] < 1 || position[1] > 5) {
                System.out.println("Errore, inserisci un indice corretto");
                position[1] = input.nextInt();
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
                scelta = input.nextInt();
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
                scelta = input.nextInt();
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
        System.out.print("Scelta: ");
        int i = 3;
        do{
            i = input.nextInt();
        }while(i < 0 || i > 6);
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
                    k = input.nextInt();
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
            default:
                setChanged();
                notifyObservers(new ClientMessage(new PlayerMove(Tool.SKIPTURN)));
                System.out.println("Mossa inviata");

        }
    }
    @Override
    public void update(Observable o, Object arg) {
        switch (((ServerMessage) arg).getMessageType()){
            case ERROR:
                System.out.println("Errore: " + ((ServerMessage) arg).getErrorType().toString());
                break;
            case INITIALCONFIGSERVER:
                this.modelView = ((ServerMessage) arg).getModelView();
                if(modelView.getPlayer(localID).isYourTurn()){
                    chooseMove();
                }
                break;
            case CHOSENBOARD:
                //da aggiungere
                break;
            case MODELVIEWUPDATE:
                //da aggiungere
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
            i = input.nextInt();
        }while(i < 1 || i > 2);

        String address = "";
        if(i == 1){
            int port = 0;
            while(!clientNetwork.isConnected()) {
                System.out.println("Inserisci l'indirizzo del server: ");
                address = input.next();
                System.out.println("Inserisci la porta: ");
                port = input.nextInt();
                clientNetwork.connectSocket(address, port);
            }
            System.out.println("Connessione accettata");
        }else{
            while(!clientNetwork.isConnected()) {
                System.out.println("Inserisci l'indirizzo del server: ");
                address = input.next();
                clientNetwork.connectRMI(address);
            }
            System.out.println("Connessione accettata");
        }
        String nick = "";
        System.out.println("Inserisci il tuo nome: ");
        do{
            nick = input.next();
        }while(nick.equals(""));
        localID = (new Random()).nextLong();

        ClientMessage clientMessage = new ClientMessage(nick,localID);
        if(clientNetwork.sendMessage(clientMessage)){
            System.out.println("Nome utente inviato");
        }else{
            System.out.println("Errore connessione");
        }

        ClientMessage testMessage = new ClientMessage(new PlayerMove(Tool.SKIPTURN));
        clientNetwork.sendMessage(testMessage);
    }
}
