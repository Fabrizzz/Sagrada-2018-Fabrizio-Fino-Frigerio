package it.polimi.se2018.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller GUI
 * @author Matteo
 */
public class ControllerGUI implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView imageView;

    @FXML
    private Button buttonSocket;

    @FXML
    private Button buttonRMI;

    /**
     * Switch scene
     * @param event
     */
    @FXML
    public void handleButton(ActionEvent event) {

        Stage stage;
        Parent newScene;
        Scene scene = null;

        if (event.getSource() == buttonSocket){
            //System.out.println("SOCKET!");
            stage = (Stage) buttonSocket.getScene().getWindow();
            try{
                newScene = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlSocket.fxml"));
                scene = new Scene(newScene);
            }
            catch (Exception e){
                System.out.println("File FXML not found");
            }
            stage.setTitle("Socket");
            stage.setScene(scene);
        }
        else {
            //System.out.println("RMI!");
            stage = (Stage) buttonRMI.getScene().getWindow();try{
                newScene = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlRMI.fxml"));
                scene = new Scene(newScene);
            }
            catch (Exception e){
                System.out.println("File FXML not found");
            }
            stage.setTitle("RMI");
            stage.setScene(scene);
        }
        stage.setResizable(false);
        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //if stage is resizable
        imageView.fitHeightProperty().bind(root.heightProperty());
        imageView.fitWidthProperty().bind(root.widthProperty());



    }

}
