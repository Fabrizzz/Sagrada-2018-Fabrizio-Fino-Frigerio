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
        System.out.println("___________");

        for(int j = 0; j < 4; j ++){
            for(int i = 0; i < 5; i++){
                try{
                    System.out.print("|" + getColor(playerBoard.getDie(j,i).getColor()) + playerBoard.getDie(j,i).getNumber().getInt() + ANSI_RESET);
                }catch(NoDieException e) {
                    System.out.print("| ");
                }
            }
            System.out.println("|");
        }
        System.out.println("‾‾‾‾‾‾‾‾‾");

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

    public void normalMove(){

        System.out.println("Scegli il dado dalla riserva");
        showDraftPool();
        System.out.println("Inserisci la posizione del dado scelto");
        int i = input.nextInt();
        while(i < 1 || i > modelView.DraftPoolSize()){
            System.out.println("Errore, inserisci una posizione corretta");
            i = input.nextInt();
        }

        showBoard(modelView.getBoard(modelView.getPlayer(localID)));

        //ClientMessage clientMessage = new ClientMessage(new PlayerMove(i,));
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
