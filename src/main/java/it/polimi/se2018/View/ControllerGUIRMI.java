package it.polimi.se2018.View;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    private static final int limitIPRMI= 15;

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

        //Max length textfield ip
        textIPRMI.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue()){
                    if (textIPRMI.getText().length() >= limitIPRMI ){
                        textIPRMI.setText(textIPRMI.getText().substring(0, limitIPRMI));
                    }
                }

            }
        });

    }
}
