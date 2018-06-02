package it.polimi.se2018.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller GUI
 * @author Matteo
 */
public class GUIController implements Initializable {


    /*
    @FXML
    private VBox vBox;
    */

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView imageView;

    @FXML
    private void handleButtonSocket(ActionEvent event) {

        System.out.println("SOCKET!");

        //Change Scene
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root;
        Scene scene = null;
        try{
            root = FXMLLoader.load(getClass().getResource("/fxmlFile/fxmlGUI.fxml"));       //Da cambiare: deve richiamare i nuovi FXML che sono da fare
            scene = new Scene(root);
        }
        catch (Exception e){
            System.out.println("File FXML not found");
        }
        window.setScene(scene);
        window.show();
    }

    @FXML
    private void handleButtonRMI(ActionEvent event){
        System.out.println("RMI!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageView.fitHeightProperty().bind(root.heightProperty());
        imageView.fitWidthProperty().bind(root.widthProperty());

    }

}
