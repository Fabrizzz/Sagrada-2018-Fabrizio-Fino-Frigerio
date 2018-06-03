package it.polimi.se2018;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.server.Server;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Cosa vuoi avviare: ");
        System.out.println("1) Client");
        System.out.println("2) Server");
        int i = 0;
        do{
            i = input.nextInt();
        }while(i < 1 || i > 2);

        if(i == 1){
            Client client = new Client();
        }else{
            Server server = new Server();
        }
    }
}
