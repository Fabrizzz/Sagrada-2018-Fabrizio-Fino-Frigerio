package it.polimi.se2018;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.server.Server;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.*;


public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Cosa vuoi avviare: ");
        System.out.println("1) Client");
        System.out.println("2) Server");
        int i = 0;
        do{
            if(input.hasNextInt()) {
                i = input.nextInt();
            }else{
                input.next();
                System.out.println("Input non corretto");
            }
        }while(i < 1 || i > 2);

        if(i == 1){
            Client client = new Client();
        }else{
            Server server = new Server();
        }
    }
}
