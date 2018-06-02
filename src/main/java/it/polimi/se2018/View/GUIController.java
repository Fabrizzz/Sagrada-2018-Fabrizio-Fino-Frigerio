package it.polimi.se2018.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    private void handleButtonSocket(ActionEvent event){
        System.out.println("SOCKET!");
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
