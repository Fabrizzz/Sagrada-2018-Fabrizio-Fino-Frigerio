package it.polimi.se2018.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller GUI Waiting
 * @author Matteo
 */
public class ControllerWaiting implements Initializable {

    @FXML
    private Button buttonWaiting;

    public void handleButton(ActionEvent event) {
        Stage stage;
        Parent newScene;
        Scene scene = null;

        stage = (Stage) buttonWaiting.getScene().getWindow();
        try{
            newScene = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlGame.fxml"));
            scene = new Scene(newScene);
        }
        catch (Exception e){
            System.out.println("File FXML not found");
        }
        stage.setTitle("Sagrada");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
