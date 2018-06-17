package it.polimi.se2018.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import static javafx.fxml.FXMLLoader.*;

/**
 * GUI
 * @author Matteo
 */
public class GUI extends Application {

    public static final CountDownLatch latch = new CountDownLatch(1);
    public static GUI classe = null;

    public static GUI waitStartUpGUI() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Errore");
            Thread.currentThread().interrupt();
        }
        return classe;
    }

    public static void setStartUpGUI(GUI startUp) {
        classe = startUp;
        latch.countDown();
    }

    public GUI() {
        setStartUpGUI(this);
    }

    public void messageStartUpGUI() {
        System.out.println("Avvio della GUI");
    }

    /**
     * Contains the code for the JavaFX Application
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        Parent root;
        Scene scene = null;
        try{
            root = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlGUI.fxml"));
            scene = new Scene(root,600,400);
        }
        catch (Exception e){
            System.out.println("File FXML not found");
        }

        primaryStage.setTitle("Sagrada");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /*
    public static void main(String[] args) {
        launch(args);
    }
    */
}
