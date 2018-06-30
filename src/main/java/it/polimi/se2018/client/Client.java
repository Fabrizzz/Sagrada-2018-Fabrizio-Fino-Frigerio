package it.polimi.se2018.client;

import it.polimi.se2018.view.CLI;
import it.polimi.se2018.view.GUIProxy;
import it.polimi.se2018.view.View;

import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.*;

/**
 * Client main class
 * @author Alessio
 */
public class Client {
    private View view;
    private ClientNetwork clientNetwork;
    private static final Logger LOGGER = Logger.getLogger("Logger");

    /**
     * Constructor
     */
    public Client(){

        LOGGER.setLevel(Level.FINEST);

        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setUseParentHandlers(false);

        FileHandler fh;
        try {
            fh = new FileHandler("sagradaClient.log");
            fh.setLevel(Level.FINEST);
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            LOGGER.log(Level.WARNING,"Security error exception");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,"Impossibile aprire file di log");
        }

        Long localID = readID();

        int i;
        Scanner input = new Scanner(System.in);
        System.out.println("Scegli l'interfaccia grafica:");
        System.out.println("1) CLI");
        System.out.println("2) GUI");
        do{
            i = input.nextInt();
        }while(i < 1 || i > 2);

        switch (i){
            case 1:
                clientCLI(localID);
                break;
            case 2:
                clientGUI();
                break;
        }
    }

    /**
     * Initialize the command line interface
     */
    public void clientCLI(Long localID){
        view = new CLI(localID);
        clientNetwork = new ClientNetwork(view);
        ((CLI) view).createConnection(clientNetwork);
    }

    /**
     * Initialize GUI
     */
    private void clientGUI() {
        view = new GUIProxy();
        clientNetwork = new ClientNetwork(view);
        ((GUIProxy) view).setClientNetwork(clientNetwork);
    }

    /**
     * Read the playerid from the file, if there is no file the id is generated and written to file
     * @return the id of the local player
     */
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

        return writeID(generateID());
    }

    /**
     * Write the id to file
     * @param id id to write
     * @return the id written
     */
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

    /**
     * Generate the id
     * @return the id
     */
    private Long generateID(){
        return (new Random()).nextLong();
    }
}
