package it.polimi.se2018.utils;

import java.util.Scanner;

public class InputUtils {
    private InputUtils(){}

    public static synchronized int getInt(){
        int i = 0;
        Scanner scanner = new Scanner(System.in);

        try{
            i = scanner.nextInt();
        }catch (Exception e){
            scanner.reset();
            System.out.println("Input non corretto, inserire un numero");
        }

        return i;
    }

    public static synchronized String getString(){
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
