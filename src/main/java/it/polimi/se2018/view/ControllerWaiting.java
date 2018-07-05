package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
import it.polimi.se2018.utils.messages.ServerMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller GUI Waiting
 * @author Matteo
 */
public class ControllerWaiting implements Initializable {

    private ServerMessage message;

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
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (message == null){
            System.out.println("NULL");
        }
        else{
            System.out.println("NOT NULL");
        }

    }

    public void sendInfo(ServerMessage message) {
        this.message = message;
    }
}
