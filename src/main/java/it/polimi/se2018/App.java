package it.polimi.se2018;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.server.Server;
import it.polimi.se2018.utils.InputUtils;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.Scanner;


public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int i = 0;

        OptionParser parser = new OptionParser("gsc");
        OptionSet options = parser.parse(args);

        if(options.has( "s" )){
            Server server = new Server();
        }else if(options.has( "g" ) || options.has( "c" ) ){
            Client client = new Client(options);
        }else {
            System.out.println("Cosa vuoi avviare: ");
            System.out.println("1) Client");
            System.out.println("2) Server");
            do {
                i = InputUtils.getInt();
            } while (i < 1 || i > 2);

            if (i == 1) {
                Client client = new Client(options);
            } else {
                Server server = new Server();
            }
        }
    }

}
