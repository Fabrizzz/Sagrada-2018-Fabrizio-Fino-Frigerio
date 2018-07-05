package it.polimi.se2018.view;

import it.polimi.se2018.client.ClientNetwork;
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

    private ClientNetwork clientNetwork;
    ControllerGUISocket nextControllerSocket;
    ControllerGUIRMI nextControllerRMI;

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
        FXMLLoader loader = null;

        nextControllerSocket = null;
        nextControllerRMI = null;

        //SOCKET
        if (event.getSource() == buttonSocket){
            stage = (Stage) buttonSocket.getScene().getWindow();
            try{
                loader = new FXMLLoader(getClass().getResource("/fxmlFile/fxmlSocket.fxml"));
                newScene = (Parent) loader.load();
                scene = new Scene(newScene);
            }
            catch (Exception e){
                System.out.println("File FXML not found");
            }
            nextControllerSocket = loader.getController();
            nextControllerSocket.sendInfo(clientNetwork);
            stage.setTitle("Socket");
            stage.setScene(scene);
        }

        //RMI
        else {
            stage = (Stage) buttonRMI.getScene().getWindow();
            try{
                loader = new FXMLLoader(getClass().getResource("/fxmlFile/fxmlRMI.fxml"));
                newScene = (Parent) loader.load();
                scene = new Scene(newScene);
            }
            catch (Exception e){
                System.out.println("File FXML not found");
            }
            nextControllerRMI = loader.getController();
            nextControllerRMI.sendInfo(clientNetwork);
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

    public void sendInfo(ClientNetwork temp) {
        clientNetwork = temp;
    }

}
