package it.polimi.se2018.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller GUI RMI
 * @author Matteo
 */
public class ControllerGUIRMI implements Initializable {

    @FXML
    private Label labelIPRMI;

    @FXML
    private Label labelNickRMI;

    @FXML
    private TextField textIPRMI;

    @FXML
    private TextField textNickRMI;

    @FXML
    private Button buttonIndietroRMI;

    @FXML
    private Button buttonAvantiRMI;

    public void handleButtonIndietroRMI(ActionEvent event) {
        Stage stage;
        Parent newScene;
        Scene scene = null;

        stage = (Stage) buttonIndietroRMI.getScene().getWindow();
        try{
            newScene = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlGUI.fxml"));
            scene = new Scene(newScene);
        }
        catch (Exception e){
            System.out.println("File FXML not found");
        }
        stage.setTitle("Sagrada");
        stage.setScene(scene);
        stage.show();
    }

    public void handleButtonAvantiRMI(ActionEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
