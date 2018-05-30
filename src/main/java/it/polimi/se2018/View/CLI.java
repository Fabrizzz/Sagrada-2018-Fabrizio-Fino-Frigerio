package it.polimi.se2018.View;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.ClientMessage;
import it.polimi.se2018.utils.PlayerMove;
import it.polimi.se2018.utils.enums.BoardName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.*;

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

    public CLI(){
        input = new Scanner(System.in);
    }

    public void print(String string){
        System.out.println(string);
    }

    public String getNickname(){
        print("Inserire nickname:");
        return(input.next());
    }

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

    public int chooseDraftpoolDie(){
        System.out.println("Scegli il dado dalla riserva");
        showDraftPool();
        System.out.println("Inserisci la posizione del dado scelto");
        int i = input.nextInt();
        while (i < 1 || i > modelView.DraftPoolSize()) {
            System.out.println("Errore, inserisci una posizione corretta");
            i = input.nextInt();
        }
        return i;
    }

    public int[] chooseBoardDie(){
        int[] position = new int[2];

        showBoard(modelView.getBoard(modelView.getPlayer(localID)));
        System.out.println("Inserisci l'indice di riga in cui vuoi positionare il dado: ");
        position[0] = input.nextInt();
        while (position[0] < 1 || position[0] > 4) {
            System.out.println("Errore, inserisci un indice corretto");
            position[0] = input.nextInt();
        }

        System.out.println("Inserisci l'indice di colonna in cui vuoi positionare il dado: ");
        position[1] = input.nextInt();
        while (position[1] < 1 || position[1] > 5) {
            System.out.println("Errore, inserisci un indice corretto");
            position[1] = input.nextInt();
        }

        return position;
    }

    public void normalSugheroMove(Tool tool){
        if(tool == Tool.MOSSASTANDARD || tool == Tool.RIGAINSUGHERO) {

            int i = chooseDraftpoolDie();

            int[] position = chooseBoardDie();

            ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool, position[0], position[1], i));
            setChanged();
            notifyObservers(clientMessage);
            System.out.println("Mossa inviata");
        }
    }

    public void skipMartellettoTenagliaMove(Tool tool){
        if(tool == Tool.SKIPTURN || tool == Tool.MARTELLETTO || tool == Tool.TENAGLIAAROTELLE) {
            ClientMessage clientMessage = new ClientMessage(new PlayerMove(tool));
            setChanged();
            notifyObservers();
            System.out.println("Mossa inviata");
        }
    }

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

            return true;
       }catch(NoDieException e){
           return false;
       }

    }

    

    @Override
    public void update(Observable o, Object arg) {

    }
}
