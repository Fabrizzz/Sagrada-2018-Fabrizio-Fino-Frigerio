package it.polimi.se2018.View;

import it.polimi.se2018.model.DiceBag;
import it.polimi.se2018.model.DraftPool;
import it.polimi.se2018.model.PlayerBoard;
import it.polimi.se2018.model.cell.ColorRestriction;
import it.polimi.se2018.model.cell.Die;
import it.polimi.se2018.model.cell.NumberRestriction;
import it.polimi.se2018.utils.enums.BoardName;
import it.polimi.se2018.utils.enums.Color;
import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;
import it.polimi.se2018.utils.exceptions.AlredySetDie;
import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.*;

public class CLI implements View{
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_BLUE = "\u001B[34m";
    private final String ANSI_PURPLE = "\u001B[35m";
    private Scanner input;

    public static void main(String[] args){
        CLI cli = new CLI();
        System.out.print(cli.getNickname());
        PlayerBoard playerBoard = new PlayerBoard(BoardName.KALEIDOSCOPICDREAM);
        try{
            playerBoard.setDie(new Die(Color.BLUE),0,0);
        }catch(AlredySetDie e){}
        try{
            playerBoard.setDie(new Die(Color.BLUE),0,1);
        }catch(AlredySetDie e){}
        try{
            playerBoard.setDie(new Die(Color.BLUE),0,2);
        }catch(AlredySetDie e){}
        try{
            playerBoard.setDie(new Die(Color.BLUE),0,3);
        }catch(AlredySetDie e){}
        try{
            playerBoard.setDie(new Die(Color.BLUE),0,4);
        }catch(AlredySetDie e){}
        try{
            playerBoard.setDie(new Die(Color.BLUE),0,0);
        }catch(AlredySetDie e){}
        try{
            playerBoard.setDie(new Die(Color.BLUE),1,1);
        }catch(AlredySetDie e){}
        try{
            playerBoard.setDie(new Die(Color.BLUE),1,2);
        }catch(AlredySetDie e){}
        try{
            playerBoard.setDie(new Die(Color.BLUE),1,3);
        }catch(AlredySetDie e){}
        try{
            playerBoard.setDie(new Die(Color.BLUE),3,4);
        }catch(AlredySetDie e){}

        cli.printBoard(playerBoard);

        DiceBag diceBag = new DiceBag(19);
        DraftPool draftPool = new DraftPool(4,diceBag);
        cli.showDraftPool(draftPool);

        Map<Tool, Boolean> tools = new EnumMap<>(Tool.class);
        Tool.getRandTools(3).forEach(k -> tools.put(k, false));
        cli.showToolCards(tools);
    }
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
    public void printBoard(PlayerBoard playerBoard){
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

    public void showDraftPool(DraftPool draftPool){
        System.out.println("Draftpool");

        System.out.print("Positione:");
        for(int i = 0; i < draftPool.size();i++){
            System.out.print("|"+(i+1));
        }
        System.out.println("|");

        System.out.print("Dadi:     ");
        for(int i = 0; i < draftPool.size(); i++){
            try{
                System.out.print("|" + getColor(draftPool.getDie(i).getColor()) + draftPool.getDie(i).getNumber().getInt() + ANSI_RESET);
            }catch(NoDieException e){
                System.out.print("| ");
            }
        }
        System.out.println("|");
    }

    public void showToolCards(Map<Tool, Boolean> tools){
        System.out.println("Tool cards");
        for (int i = 0; i < tools.size(); i++) {
            System.out.println("Nome:");
            System.out.println(tools.keySet().toArray()[i].toString());
            System.out.println("Descrizione:");
            System.out.println("Usato: "+tools.get(tools.keySet().toArray()[i]) + "\n");
        }
    }
}
