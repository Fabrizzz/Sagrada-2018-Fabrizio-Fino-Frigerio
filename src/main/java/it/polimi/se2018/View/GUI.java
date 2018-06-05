package it.polimi.se2018.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static javafx.fxml.FXMLLoader.*;

/**
 * GUI
 * @author Matteo
 */
public class GUI extends Application {

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
            scene = new Scene(root);
        }
        catch (Exception e){
            System.out.println("File FXML not found");
        }

        primaryStage.setTitle("Sagrada");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
    public static void main(String[] args) {
        launch(args);
    }
    */
}
