package it.polimi.se2018.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller GUI Socket
 * @author Matteo
 */
public class ControllerGUISocket implements Initializable {

    @FXML
    private Button buttonIndietroSocket;

    @FXML
    private Button buttonAvantiSocket;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleButtonIndietroSocket(ActionEvent event) {
        Stage stage;
        Parent newScene;
        Scene scene = null;

        stage = (Stage) buttonIndietroSocket.getScene().getWindow();
        try{
            newScene = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlGUI.fxml"));
            scene = new Scene(newScene);
        }
        catch (Exception e){
            System.out.println("File FXML not found");
        }
        stage.setTitle("GUI");
        stage.setScene(scene);
        stage.show();
    }

    public void handleButtonAvantiSocket(ActionEvent event) {
    }
}
