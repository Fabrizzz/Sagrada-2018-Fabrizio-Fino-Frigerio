package it.polimi.se2018.utils;

import java.util.Scanner;

public class InputUtils {
    private InputUtils(){}

    public static synchronized int getInt(){
        int i = -1;
        Scanner scanner = new Scanner(System.in);
        while (i < 0) {
            try {
                i = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                scanner.reset();
                scanner.next();
                System.out.println("Input non corretto, inserire un numero");
            }
            if (i < 0)
                System.out.println("Inserire un numero maggiore di 0");
        }
        return i;
    }

    public static synchronized String getString(){
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
